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

package net.fabricmc.fabric.test.resource.loader;

import java.util.Collection;
import java.util.Collections;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

public class ResourceReloadListenerTestMod implements ModInitializer {
	public static final String MODID = "fabric-resource-loader-v0-testmod";

	private static boolean clientResources = false;
	private static boolean serverResources = false;

	@Override
	public void onInitialize() {
		setupClientReloadListeners();
		setupServerReloadListeners();

		ServerTickEvents.START_WORLD_TICK.register(world -> {
			if (!clientResources && FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
				throw new AssertionError("Client reload listener was not called.");
			}

			if (!serverResources) {
				throw new AssertionError("Server reload listener was not called.");
			}
		});
	}

	private void setupClientReloadListeners() {
		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public ResourceLocation getFabricId() {
				return ResourceLocation.fromNamespaceAndPath(MODID, "client_second");
			}

			@Override
			public void onResourceManagerReload(ResourceManager manager) {
				if (!clientResources) {
					throw new AssertionError("Second reload listener was called before the first!");
				}
			}

			@Override
			public Collection<ResourceLocation> getFabricDependencies() {
				return Collections.singletonList(ResourceLocation.fromNamespaceAndPath(MODID, "client_first"));
			}
		});

		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public ResourceLocation getFabricId() {
				return ResourceLocation.fromNamespaceAndPath(MODID, "client_first");
			}

			@Override
			public void onResourceManagerReload(ResourceManager manager) {
				clientResources = true;
			}
		});
	}

	private void setupServerReloadListeners() {
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public ResourceLocation getFabricId() {
				return ResourceLocation.fromNamespaceAndPath(MODID, "server_second");
			}

			@Override
			public void onResourceManagerReload(ResourceManager manager) {
				if (!serverResources) {
					throw new AssertionError("Second reload listener was called before the first!");
				}
			}

			@Override
			public Collection<ResourceLocation> getFabricDependencies() {
				return Collections.singletonList(ResourceLocation.fromNamespaceAndPath(MODID, "server_first"));
			}
		});

		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public ResourceLocation getFabricId() {
				return ResourceLocation.fromNamespaceAndPath(MODID, "server_first");
			}

			@Override
			public void onResourceManagerReload(ResourceManager manager) {
				serverResources = true;
			}
		});
	}
}
