package com.example.random_reimagined_renovations.Main;

import com.example.random_reimagined_renovations.CustomBlockClasses.BedrockBreaker;
import com.example.random_reimagined_renovations.CustomBlockClasses.TntDuperBlock;
import com.example.random_reimagined_renovations.CustomBlockClasses.entity.FreezerBlock;
import com.example.random_reimagined_renovations.RandomReimaginedRenovations;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class CustomBlocks {
    public static final Block TNT_DUPER = registerBlock("tnt_duper",
            new TntDuperBlock(FabricBlockSettings.copy(net.minecraft.block.Blocks.DISPENSER).strength(2.5f).hardness(2.5f).requiresTool()), ItemGroups.REDSTONE);
    public static final Block BEDROCK_BREAKER = registerBlock("bedrock_breaker",
            new BedrockBreaker(FabricBlockSettings.copy(net.minecraft.block.Blocks.OBSIDIAN).strength(2.5f).hardness(2.5f).requiresTool()), ItemGroups.REDSTONE);
    public static final Block FREEZER = registerBlock("freezer",
            new FreezerBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).nonOpaque()), ItemGroups.FUNCTIONAL);
    private static Block registerBlock(String name, Block block, RegistryKey<ItemGroup> tab) {
        registerBlockItem(name, block, tab);
        return Registry.register(Registries.BLOCK, new Identifier(RandomReimaginedRenovations.MOD_ID, name), block);
    }
    private static Item registerBlockItem(String name, Block block, RegistryKey<ItemGroup> tab) {
        Item item = Registry.register(Registries.ITEM, new Identifier(RandomReimaginedRenovations.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
        ItemGroupEvents.modifyEntriesEvent(tab).register(entries -> entries.add(item));
        return item;
    }

    public static void registerModBlocks() {
        RandomReimaginedRenovations.LOGGER.debug("Registering ModBlocks for " + RandomReimaginedRenovations.MOD_ID);
    }
}
