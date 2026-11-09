package com.example.retro16;

import net.fabricmc.api.ClientModInitializer;

public class Retro16Client implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		Retro16.LOGGER.info("[Retro16] Client init: forcing flat lighting and a 16-bit (RGB5_A1) color buffer.");
	}
}
