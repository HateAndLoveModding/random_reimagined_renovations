package com.example.random_reimagined_renovations.CustomBlockClasses;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CobwebBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CloudBlock extends CobwebBlock {
    public CloudBlock(Settings settings) {
        super(settings);
    }
    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        entity.slowMovement(state, new Vec3d(0.999, 0.999, 0.999));
    }
}