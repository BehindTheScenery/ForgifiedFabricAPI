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

package net.fabricmc.fabric.test.object.builder;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.type.BlockSetTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class TealSignTest implements ModInitializer {
	public static final ResourceLocation TEAL_TYPE_ID = ObjectBuilderTestConstants.id("teal");
	public static final BlockSetType TEAL_BLOCK_SET_TYPE = BlockSetTypeBuilder.copyOf(BlockSetType.OAK).build(TEAL_TYPE_ID);
	public static final WoodType TEAL_WOOD_TYPE = WoodTypeBuilder.copyOf(WoodType.OAK).build(TEAL_TYPE_ID, TEAL_BLOCK_SET_TYPE);
	public static final StandingSignBlock TEAL_SIGN = new StandingSignBlock(TEAL_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN)) {
		@Override
		public TealSign newBlockEntity(BlockPos pos, BlockState state) {
			return new TealSign(pos, state);
		}
	};
	public static final WallSignBlock TEAL_WALL_SIGN = new WallSignBlock(TEAL_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN)) {
		@Override
		public TealSign newBlockEntity(BlockPos pos, BlockState state) {
			return new TealSign(pos, state);
		}
	};
	public static final CeilingHangingSignBlock TEAL_HANGING_SIGN = new CeilingHangingSignBlock(TEAL_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_HANGING_SIGN)) {
		@Override
		public TealHangingSign newBlockEntity(BlockPos pos, BlockState state) {
			return new TealHangingSign(pos, state);
		}
	};
	public static final WallHangingSignBlock TEAL_WALL_HANGING_SIGN = new WallHangingSignBlock(TEAL_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_HANGING_SIGN)) {
		@Override
		public TealHangingSign newBlockEntity(BlockPos pos, BlockState state) {
			return new TealHangingSign(pos, state);
		}
	};
	public static final SignItem TEAL_SIGN_ITEM = new SignItem(new Item.Properties(), TEAL_SIGN, TEAL_WALL_SIGN);
	public static final HangingSignItem TEAL_HANGING_SIGN_ITEM = new HangingSignItem(TEAL_HANGING_SIGN, TEAL_WALL_HANGING_SIGN, new Item.Properties());
	public static final BlockEntityType<TealSign> TEST_SIGN_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(TealSign::new, TEAL_SIGN, TEAL_WALL_SIGN).build();
	public static final BlockEntityType<TealHangingSign> TEST_HANGING_SIGN_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(TealHangingSign::new, TEAL_HANGING_SIGN, TEAL_WALL_HANGING_SIGN).build();

	@Override
	public void onInitialize() {
		WoodType.register(TEAL_WOOD_TYPE);

		Registry.register(BuiltInRegistries.BLOCK, ObjectBuilderTestConstants.id("teal_sign"), TEAL_SIGN);
		Registry.register(BuiltInRegistries.BLOCK, ObjectBuilderTestConstants.id("teal_wall_sign"), TEAL_WALL_SIGN);
		Registry.register(BuiltInRegistries.BLOCK, ObjectBuilderTestConstants.id("teal_hanging_sign"), TEAL_HANGING_SIGN);
		Registry.register(BuiltInRegistries.BLOCK, ObjectBuilderTestConstants.id("teal_wall_hanging_sign"), TEAL_WALL_HANGING_SIGN);

		Registry.register(BuiltInRegistries.ITEM, ObjectBuilderTestConstants.id("teal_sign"), TEAL_SIGN_ITEM);
		Registry.register(BuiltInRegistries.ITEM, ObjectBuilderTestConstants.id("teal_hanging_sign"), TEAL_HANGING_SIGN_ITEM);

		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ObjectBuilderTestConstants.id("teal_sign"), TEST_SIGN_BLOCK_ENTITY);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ObjectBuilderTestConstants.id("teal_hanging_sign"), TEST_HANGING_SIGN_BLOCK_ENTITY);
	}

	public static class TealSign extends SignBlockEntity {
		public TealSign(BlockPos pos, BlockState state) {
			super(pos, state);
		}

		@Override
		public BlockEntityType<?> getType() {
			return TEST_SIGN_BLOCK_ENTITY;
		}
	}

	public static class TealHangingSign extends HangingSignBlockEntity {
		public TealHangingSign(BlockPos pos, BlockState state) {
			super(pos, state);
		}

		@Override
		public BlockEntityType<?> getType() {
			return TEST_HANGING_SIGN_BLOCK_ENTITY;
		}
	}
}
