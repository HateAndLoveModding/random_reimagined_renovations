package com.example.random_reimagined_renovations.recipe;

import com.example.random_reimagined_renovations.RandomReimaginedRenovations;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {
    public static void registerRecipes() {
        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(RandomReimaginedRenovations.MOD_ID, FreezerRecipe.Serializer.ID),
                FreezerRecipe.Serializer.INSTANCE);
        Registry.register(Registries.RECIPE_TYPE, new Identifier(RandomReimaginedRenovations.MOD_ID, FreezerRecipe.Type.ID),
                FreezerRecipe.Type.INSTANCE);
    }
}
