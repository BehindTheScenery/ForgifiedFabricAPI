/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.fabric.impl.client.model.loading;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.fabric.api.client.model.loading.v1.BlockStateResolver;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelResolver;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class ModelLoadingPluginContextImpl implements ModelLoadingPlugin.Context {
	private static final Logger LOGGER = LoggerFactory.getLogger(ModelLoadingPluginContextImpl.class);

	final Set<ResourceLocation> extraModels = new LinkedHashSet<>();
	final Map<ResourceLocation, BlockStateResolver> blockStateResolvers = new HashMap<>();

	private final Event<ModelResolver> modelResolvers = EventFactory.createArrayBacked(ModelResolver.class, resolvers -> context -> {
		for (ModelResolver resolver : resolvers) {
			try {
				UnbakedModel model = resolver.resolveModel(context);

				if (model != null) {
					return model;
				}
			} catch (Exception exception) {
				LOGGER.error("Failed to resolve model", exception);
			}
		}

		return null;
	});

	private static final ResourceLocation[] MODEL_MODIFIER_PHASES = new ResourceLocation[] { ModelModifier.OVERRIDE_PHASE, ModelModifier.DEFAULT_PHASE, ModelModifier.WRAP_PHASE, ModelModifier.WRAP_LAST_PHASE };

	private final Event<ModelModifier.OnLoad> onLoadModifiers = EventFactory.createWithPhases(ModelModifier.OnLoad.class, modifiers -> (model, context) -> {
		for (ModelModifier.OnLoad modifier : modifiers) {
			try {
				model = modifier.modifyModelOnLoad(model, context);
			} catch (Exception exception) {
				LOGGER.error("Failed to modify unbaked model on load", exception);
			}
		}

		return model;
	}, MODEL_MODIFIER_PHASES);
	private final Event<ModelModifier.BeforeBake> beforeBakeModifiers = EventFactory.createWithPhases(ModelModifier.BeforeBake.class, modifiers -> (model, context) -> {
		for (ModelModifier.BeforeBake modifier : modifiers) {
			try {
				model = modifier.modifyModelBeforeBake(model, context);
			} catch (Exception exception) {
				LOGGER.error("Failed to modify unbaked model before bake", exception);
			}
		}

		return model;
	}, MODEL_MODIFIER_PHASES);
	private final Event<ModelModifier.AfterBake> afterBakeModifiers = EventFactory.createWithPhases(ModelModifier.AfterBake.class, modifiers -> (model, context) -> {
		for (ModelModifier.AfterBake modifier : modifiers) {
			try {
				model = modifier.modifyModelAfterBake(model, context);
			} catch (Exception exception) {
				LOGGER.error("Failed to modify baked model after bake", exception);
			}
		}

		return model;
	}, MODEL_MODIFIER_PHASES);

	public ModelLoadingPluginContextImpl() {
	}

	@Override
	public void addModels(ResourceLocation... ids) {
		for (ResourceLocation id : ids) {
			extraModels.add(id);
		}
	}

	@Override
	public void addModels(Collection<? extends ResourceLocation> ids) {
		extraModels.addAll(ids);
	}

	@Override
	public void registerBlockStateResolver(Block block, BlockStateResolver resolver) {
		Objects.requireNonNull(block, "block cannot be null");
		Objects.requireNonNull(resolver, "resolver cannot be null");

		Optional<ResourceKey<Block>> optionalKey = BuiltInRegistries.BLOCK.getResourceKey(block);

		if (optionalKey.isEmpty()) {
			throw new IllegalArgumentException("Received unregistered block");
		}

		ResourceLocation blockId = optionalKey.get().location();

		if (blockStateResolvers.put(blockId, resolver) != null) {
			throw new IllegalArgumentException("Duplicate block state resolver for " + block);
		}
	}

	@Override
	public Event<ModelResolver> resolveModel() {
		return modelResolvers;
	}

	@Override
	public Event<ModelModifier.OnLoad> modifyModelOnLoad() {
		return onLoadModifiers;
	}

	@Override
	public Event<ModelModifier.BeforeBake> modifyModelBeforeBake() {
		return beforeBakeModifiers;
	}

	@Override
	public Event<ModelModifier.AfterBake> modifyModelAfterBake() {
		return afterBakeModifiers;
	}
}
