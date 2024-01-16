package com.example.random_reimagined_renovations.Main;

import com.example.random_reimagined_renovations.CustomItemClasses.FiniteWater;
import com.example.random_reimagined_renovations.CustomItemClasses.InfiniteWater;
import com.example.random_reimagined_renovations.RandomReimaginedRenovations;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class CustomItems {
    public static final Item INFINITE_AGUA_BUCKET = registerItem("infinite_agua_bucket",
            new InfiniteWater(new FabricItemSettings()));
    public static final Item FINITE_AGUA_BUCKET = registerItem("finite_agua_bucket",
            new FiniteWater(new FabricItemSettings()));
    public static final Item RADIATOR = registerItem("radiator",
            new Item(new FabricItemSettings()));
    public static final Item FAN = registerItem("fan",
            new Item(new FabricItemSettings()));
    public static final Item ICE_CUBES = registerItem("ice_cubes",
            new Item(new FabricItemSettings()));
    public static final Item ICE_TRAY = registerItem("ice_tray",
            new Item(new FabricItemSettings()));
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(RandomReimaginedRenovations.MOD_ID, name), item);
    }
    public static void addItemsToItemGroups() {
        addToItemGroup(ItemGroups.FUNCTIONAL, INFINITE_AGUA_BUCKET);
        addToItemGroup(ItemGroups.FUNCTIONAL, FINITE_AGUA_BUCKET);
        addToItemGroup(ItemGroups.FUNCTIONAL, RADIATOR);
        addToItemGroup(ItemGroups.FUNCTIONAL, FAN);
        addToItemGroup(ItemGroups.FUNCTIONAL, ICE_CUBES);
        addToItemGroup(ItemGroups.FUNCTIONAL, ICE_TRAY);
    }
    public static void addToItemGroup(RegistryKey<ItemGroup> group, Item item) {
        ItemGroupEvents.modifyEntriesEvent(group).register(entries -> entries.add(item));
    }
    public static void registerModItems() {
        RandomReimaginedRenovations.LOGGER.debug("Registering Mod Items for " + RandomReimaginedRenovations.MOD_ID);
        addItemsToItemGroups();
    }
}
