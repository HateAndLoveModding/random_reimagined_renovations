package com.example.random_reimagined_renovations.CustomBlockClasses;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.TntEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TntDuperBlock extends Block {
    public long currentTick;
    public long previousTick;
    public TntDuperBlock(Settings settings) {
        super(settings);
    }
    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        currentTick = world.getTime();
        if (world.isReceivingRedstonePower(pos) && currentTick > previousTick + 20) {
            BlockPos tntPos = pos.down();
            world.spawnEntity(new TntEntity(world, tntPos.getX() + .5, tntPos.getY(), tntPos.getZ() + .5, null));
            previousTick = currentTick;
        }
    }

}
