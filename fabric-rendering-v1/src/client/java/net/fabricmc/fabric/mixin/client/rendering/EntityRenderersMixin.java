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

package net.fabricmc.fabric.mixin.client.rendering;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.impl.client.rendering.RegistrationHelperImpl;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRenderers.class)
public abstract class EntityRenderersMixin {
	// synthetic lambda in reloadEntityRenderers
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Redirect(method = {"lambda$createEntityRenderers$26", "lambda$createEntityRenderers$2"}, require = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRendererProvider;create(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;)Lnet/minecraft/client/renderer/entity/EntityRenderer;"))
	private static EntityRenderer<?> createEntityRenderer(EntityRendererProvider<?> entityRendererFactory, EntityRendererProvider.Context context, ImmutableMap.Builder builder, EntityRendererProvider.Context context2, EntityType<?> entityType) {
		EntityRenderer<?> entityRenderer = entityRendererFactory.create(context);

		if (entityRenderer instanceof LivingEntityRenderer) { // Must be living for features
			LivingEntityRendererAccessor accessor = (LivingEntityRendererAccessor) entityRenderer;
			LivingEntityFeatureRendererRegistrationCallback.EVENT.invoker().registerRenderers((EntityType<? extends LivingEntity>) entityType, (LivingEntityRenderer) entityRenderer, new RegistrationHelperImpl(accessor::callAddFeature), context);
		}

		return entityRenderer;
	}

	// private static synthetic method_32175(Lcom/google/common/collect/ImmutableMap$Builder;Lnet/minecraft/class_5617$class_5618;Ljava/lang/String;Lnet/minecraft/class_5617;)V
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Redirect(method = {"lambda$createPlayerRenderers$27", "lambda$createPlayerRenderers$3"}, require = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRendererProvider;create(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;)Lnet/minecraft/client/renderer/entity/EntityRenderer;"))
	private static EntityRenderer<? extends Player> createPlayerEntityRenderer(EntityRendererProvider playerEntityRendererFactory, EntityRendererProvider.Context context) {
		EntityRenderer<? extends Player> entityRenderer = playerEntityRendererFactory.create(context);

		LivingEntityRendererAccessor accessor = (LivingEntityRendererAccessor) entityRenderer;
		LivingEntityFeatureRendererRegistrationCallback.EVENT.invoker().registerRenderers(EntityType.PLAYER, (LivingEntityRenderer) entityRenderer, new RegistrationHelperImpl(accessor::callAddFeature), context);

		return entityRenderer;
	}
}
