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

package net.fabricmc.fabric.mixin.resource.loader;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import net.fabricmc.fabric.impl.resource.loader.ModResourcePackCreator;
import net.minecraft.network.protocol.configuration.ServerboundSelectKnownPacks;

@Mixin(ServerboundSelectKnownPacks.class)
public class SelectKnownPacksC2SPacketMixin {
	@ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/codec/ByteBufCodecs;list(I)Lnet/minecraft/network/codec/StreamCodec$CodecOperation;"))
	private static int setMaxKnownPacks(int constant) {
		return ModResourcePackCreator.MAX_KNOWN_PACKS;
	}
}
