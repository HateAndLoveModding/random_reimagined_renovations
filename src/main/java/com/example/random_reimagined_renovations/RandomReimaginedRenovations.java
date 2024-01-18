package com.example.random_reimagined_renovations;

import com.example.random_reimagined_renovations.CustomBlockClasses.entity.FreezerBlockEntity;
import com.example.random_reimagined_renovations.Main.CustomBlocks;
import com.example.random_reimagined_renovations.Main.CustomItems;
import com.example.random_reimagined_renovations.recipe.ModRecipes;
import com.example.random_reimagined_renovations.screen.FreezerScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomReimaginedRenovations implements ModInitializer {
	//Make sure creature carrier trades are correct
    public static final Logger LOGGER = LoggerFactory.getLogger("random_reimagined_renovations");
	public static final String MOD_ID = "random_reimagined_renovations";
	private static final DispenserBehavior customDispenserBehaviorSapling = new CustomDispenserBehavior();

	public static final ScreenHandlerType<FreezerScreenHandler> FREEZER_SCREEN_HANDLER =
			Registry.register(Registries.SCREEN_HANDLER, new Identifier(RandomReimaginedRenovations.MOD_ID, "freezing"),
					new ExtendedScreenHandlerType<>(FreezerScreenHandler::new));

	public static final BlockEntityType<FreezerBlockEntity> FREEZER_BLOCK_ENTITY =
			Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(RandomReimaginedRenovations.MOD_ID, "freezer_block_entity"),
					FabricBlockEntityTypeBuilder.create(FreezerBlockEntity::new,
							CustomBlocks.FREEZER).build());


	@Override
	public void onInitialize() {
		ModRecipes.registerRecipes();
		Item[] itemsToRegister = {Items.OAK_SAPLING, Items.SPRUCE_SAPLING, Items.ACACIA_SAPLING, Items.BIRCH_SAPLING,
				Items.CHERRY_SAPLING, Items.DARK_OAK_SAPLING, Items.JUNGLE_SAPLING, Items.WHEAT_SEEDS,
				Items.BEETROOT_SEEDS, Items.CARROT, Items.POTATO, Items.BAMBOO, Items.BROWN_MUSHROOM,
				Items.RED_MUSHROOM, Items.SUGAR_CANE, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS,
				Items.SWEET_BERRIES, Items.CRIMSON_FUNGUS, Items.WARPED_FUNGUS, Items.CHORUS_FLOWER,
				Items.COCOA_BEANS, Items.KELP, Items.NETHER_WART
		};

		for (Item item : itemsToRegister) {
			DispenserBlock.registerBehavior(item, customDispenserBehaviorSapling);
		}
		CustomBlocks.registerModBlocks();
		CustomItems.registerModItems();
	}
}