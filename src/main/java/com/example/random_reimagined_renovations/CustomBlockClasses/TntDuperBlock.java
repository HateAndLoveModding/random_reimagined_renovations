package com.example.random_reimagined_renovations.CustomBlockClasses;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.TntEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TntDuperBlock extends Block {
    public TntDuperBlock(Settings settings) {
        super(settings);
    }
    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (world.isReceivingRedstonePower(pos)) {
            BlockPos tntPos = pos.down();
            world.spawnEntity(new TntEntity(world, tntPos.getX() + 0.5, tntPos.getY(), tntPos.getZ() + 0.5, null));
        }
    }

}
