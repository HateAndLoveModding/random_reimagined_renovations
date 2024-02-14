package com.example.random_reimagined_renovations.CustomBlockClasses;

import com.example.random_reimagined_renovations.RandomReimaginedRenovations;
import com.example.random_reimagined_renovations.recipe.FreezerRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.TntEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.minecraft.inventory.Inventories.writeNbt;

public class TntDuperBlock extends Block {
    public TntDuperBlock(Settings settings) {
        super(settings);
    }
    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        NbtCompound nbt = new NbtCompound();
        long current = world.getTime();
        long last = nbt.getLong("last");

        RandomReimaginedRenovations.LOGGER.info("current - last: " + (current - last));
        RandomReimaginedRenovations.LOGGER.info("Power: " + world.isReceivingRedstonePower(pos));

        if (world.isReceivingRedstonePower(pos) && (current - last) > 20) {
            BlockPos tntPos = pos.down();
            world.spawnEntity(new TntEntity(world, tntPos.getX() + 0.5, tntPos.getY(), tntPos.getZ() + 0.5, null));
            nbt.putLong("last", current);
            // Mark the block entity as dirty to ensure NBT changes are saved
        }
    }

    private NbtCompound getOrCreateNbt() {
        NbtCompound nbt = new NbtCompound();
        if (!nbt.contains("last")) {
            nbt.putLong("last", 0);
            //markDirty();  // Mark the block entity as dirty to ensure NBT changes are saved
        }
        return nbt;
    }
}