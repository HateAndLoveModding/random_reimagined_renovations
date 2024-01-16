package com.example.random_reimagined_renovations;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import com.example.random_reimagined_renovations.screen.FreezerScreen;

import static com.example.random_reimagined_renovations.RandomReimaginedRenovations.FREEZER_SCREEN_HANDLER;

public class RandomReimaginedRenovationsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(FREEZER_SCREEN_HANDLER, FreezerScreen::new);
    }
}
