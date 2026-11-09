package com.example.retro16;

import net.fabricmc.api.ModInitializer;
import net.minecraft.world.IWorld;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Retro16 implements ModInitializer {
	public static final String MOD_ID = "retro16";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	/** Total border width/depth in blocks, centered on the world spawn. */
	public static final int BORDER_SIZE = 2000;
	public static final int BORDER_HALF = BORDER_SIZE / 2;

	/**
	 * false = entities outside the border are frozen (not ticked) but preserved.
	 * true  = entities outside the border are permanently removed (destructive).
	 */
	public static final boolean REMOVE_OUT_OF_BORDER_ENTITIES = false;

	@Override
	public void onInitialize() {
		LOGGER.info("[Retro16] Common init: {}x{} border, uncapped horse stacking.", BORDER_SIZE, BORDER_SIZE);
	}

	/** True if the block column (x, z) lies outside the 2000x2000 square around this world's spawn. */
	public static boolean isBlockOutside(IWorld world, int x, int z) {
		int spawnX = world.getLevelProperties().getSpawnX();
		int spawnZ = world.getLevelProperties().getSpawnZ();
		return Math.abs(x - spawnX) > BORDER_HALF || Math.abs(z - spawnZ) > BORDER_HALF;
	}

	/** True if the chunk's center column lies outside the border. */
	public static boolean isChunkOutside(IWorld world, int chunkX, int chunkZ) {
		return isBlockOutside(world, (chunkX << 4) + 8, (chunkZ << 4) + 8);
	}
}
