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

package net.fabricmc.fabric.mixin.loot;

import java.util.List;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.fabricmc.fabric.api.loot.v3.FabricLootTableBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

/**
 * Accesses loot table fields for {@link FabricLootTableBuilder#copyOf(LootTable)}.
 * These are normally available in the transitive access widener module.
 */
@Mixin(LootTable.class)
public interface LootTableAccessor {
	@Accessor("pools")
	List<LootPool> fabric_getPools();

	@Accessor("functions")
	List<LootItemFunction> fabric_getFunctions();

	@Accessor("randomSequence")
	Optional<ResourceLocation> fabric_getRandomSequenceId();
}
