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

package net.fabricmc.fabric.test.gametest;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;

public class ExampleTestSuite {
	@GameTest
	public void diamond(GameTestHelper context) {
		context.succeedWhen(() ->
				context.assertBlock(new BlockPos(0, 2, 0), (block) -> block == Blocks.DIAMOND_BLOCK, "Expect block to be diamond")
		);
	}

	@GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
	public void noStructure(GameTestHelper context) {
		context.setBlock(0, 2, 0, Blocks.DIAMOND_BLOCK);

		context.succeedWhen(() ->
				context.assertBlock(new BlockPos(0, 2, 0), (block) -> block == Blocks.DIAMOND_BLOCK, "Expect block to be diamond")
		);
	}
}
