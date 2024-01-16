package com.example.random_reimagined_renovations.CustomItemClasses;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InfiniteWater extends BucketItem {
    public InfiniteWater(Settings settings) {
        super(Fluids.WATER, settings);
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        HitResult result = playerEntity.raycast(5.0, 1.0F, false);

        if (!world.isClient) {
            BlockPos targetBlockPos = ((BlockHitResult) result).getBlockPos();
            try {
                if (!world.getBlockState(targetBlockPos).get(Properties.WATERLOGGED)) {
                    world.setBlockState(targetBlockPos, world.getBlockState(targetBlockPos).with(Properties.WATERLOGGED, true));
                }
            } catch (Exception e) {
                if (((BlockHitResult) result).getSide().getOffsetY() == 1) {
                    world.setBlockState(targetBlockPos.up(), Blocks.WATER.getDefaultState());
                } else if (((BlockHitResult) result).getSide().getOffsetY() == -1) {
                    world.setBlockState(targetBlockPos.down(), Blocks.WATER.getDefaultState());
                } else {
                    world.setBlockState(targetBlockPos.offset(((BlockHitResult) result).getSide()), Blocks.WATER.getDefaultState());
                }
            }
        }

        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }
}
