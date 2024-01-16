package com.example.random_reimagined_renovations;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class CustomDispenserBehavior implements DispenserBehavior {
    private final ItemDispenserBehavior defaultDispenseItemBehavior = new ItemDispenserBehavior();

    @Override
    public ItemStack dispense(BlockPointer blockPointer, ItemStack itemStack) {
        BlockPos dispenserPos = blockPointer.pos();
        Direction facing = blockPointer.state().get(DispenserBlock.FACING);
        BlockPos targetPos = dispenserPos.offset(facing);
        BlockPos targetPos2 = dispenserPos.offset(facing, 2);
        BlockState targetState = blockPointer.state();
        BlockState belowTargetState = blockPointer.world().getBlockState(targetPos.down());
        BlockState frontTargetState = blockPointer.world().getBlockState(targetPos2);
        ServerWorld serverWorld = (ServerWorld) blockPointer.world();

        if (itemStack.getItem() == Items.WHEAT_SEEDS || itemStack.getItem() == Items.BEETROOT_SEEDS ||
                itemStack.getItem() == Items.CARROT || itemStack.getItem() == Items.POTATO ||
                itemStack.getItem() == Items.MELON_SEEDS || itemStack.getItem() == Items.PUMPKIN_SEEDS) {
            placeBlock(serverWorld, itemStack, targetPos, Blocks.FARMLAND.getDefaultState());
        } else if (itemStack.getItem() == Items.OAK_SAPLING || itemStack.getItem() == Items.SPRUCE_SAPLING ||
                itemStack.getItem() == Items.ACACIA_SAPLING || itemStack.getItem() == Items.BIRCH_SAPLING ||
                itemStack.getItem() == Items.CHERRY_SAPLING || itemStack.getItem() == Items.DARK_OAK_SAPLING ||
                itemStack.getItem() == Items.JUNGLE_SAPLING || itemStack.getItem() == Items.BAMBOO ||
                itemStack.getItem() == Items.SUGAR_CANE || itemStack.getItem() == Items.BROWN_MUSHROOM ||
                itemStack.getItem() == Items.RED_MUSHROOM || itemStack.getItem() == Items.SWEET_BERRIES) {
            placeBlock(serverWorld, itemStack, targetPos, Blocks.GRASS_BLOCK.getDefaultState(), Blocks.DIRT.getDefaultState());
        } else if (itemStack.getItem() == Items.COCOA_BEANS) {
            if (targetState.isAir() && frontTargetState.equals(Blocks.JUNGLE_LOG.getDefaultState())) {
                Block block = Block.getBlockFromItem(itemStack.getItem());
                serverWorld.setBlockState(targetPos, block.getDefaultState());
                itemStack.decrement(1);
                return itemStack;
            }
        } else if (itemStack.getItem() == Items.KELP) {
            if (targetState.equals(Blocks.WATER.getDefaultState())) {
                Block block = Block.getBlockFromItem(itemStack.getItem());
                serverWorld.setBlockState(targetPos, block.getDefaultState());
                itemStack.decrement(1);
                return itemStack;
            }
        } else if (itemStack.getItem() == Items.NETHER_WART) {
            placeBlock(serverWorld, itemStack, targetPos, Blocks.SOUL_SAND.getDefaultState());
        } else if (itemStack.getItem() == Items.CRIMSON_FUNGUS) {
            placeBlock(serverWorld, itemStack, targetPos, Blocks.CRIMSON_NYLIUM.getDefaultState());
        } else if (itemStack.getItem() == Items.WARPED_FUNGUS) {
            placeBlock(serverWorld, itemStack, targetPos, Blocks.WARPED_NYLIUM.getDefaultState());
        } else if (itemStack.getItem() == Items.CHORUS_FLOWER) {
            placeBlock(serverWorld, itemStack, targetPos, Blocks.END_STONE.getDefaultState());
        }

        return defaultDispenseItemBehavior.dispense(blockPointer, itemStack);
    }

    private void placeBlock(ServerWorld world, ItemStack itemStack, BlockPos pos, BlockState... validStates) {
        for (BlockState validState : validStates) {
            if (world.getBlockState(pos).isAir() && world.getBlockState(pos.down()).equals(validState)) {
                Block block = Block.getBlockFromItem(itemStack.getItem());
                world.setBlockState(pos, block.getDefaultState());
                itemStack.decrement(1);
                break;
            }
        }
    }
}

