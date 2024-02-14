package com.example.random_reimagined_renovations.CustomItemClasses;

import com.example.random_reimagined_renovations.Main.CustomBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class CloudItem extends Item {
    public CloudItem(Settings settings) {
        super(settings);
    }
    //Add check if air
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        HitResult result = playerEntity.raycast(5.0, 1.0F, false);

        if (!world.isClient) {
            BlockPos targetBlockPos = ((BlockHitResult) result).getBlockPos();
            /*
            if (playerEntity.getHorizontalFacing().equals(Direction.EAST)) {
                world.setBlockState(targetBlockPos.add(1, -1, 0), CustomBlocks.CLOUD_BLOCK.getDefaultState());
            } else if (playerEntity.getHorizontalFacing().equals(Direction.WEST)) {
                world.setBlockState(targetBlockPos.add(-1, -1, 0), CustomBlocks.CLOUD_BLOCK.getDefaultState());
            } else if (playerEntity.getHorizontalFacing().equals(Direction.SOUTH)) {
                world.setBlockState(targetBlockPos.add(0, -1, 1), CustomBlocks.CLOUD_BLOCK.getDefaultState());
            } else if (playerEntity.getHorizontalFacing().equals(Direction.NORTH)) {
                world.setBlockState(targetBlockPos.add(0, -1, -1), CustomBlocks.CLOUD_BLOCK.getDefaultState());
            }

             */
            world.setBlockState(targetBlockPos, CustomBlocks.CLOUD_BLOCK.getDefaultState());
        }
        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }
}
