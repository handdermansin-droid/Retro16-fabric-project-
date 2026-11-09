package com.example.retro16.mixin;

import com.example.retro16.Retro16;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Chunk generation side of the 2000x2000 border: outside the square around spawn,
 * cave carving and feature population (trees, ores, structures, etc.) are skipped.
 * Tick and entity freezing outside the border lives in ServerWorldMixin.
 */
@Mixin(ChunkGenerator.class)
public abstract class ChunkGeneratorMixin<C extends ChunkGeneratorConfig> {

	@Shadow
	protected IWorld world;

	@Inject(method = "generateFeatures", at = @At("HEAD"), cancellable = true)
	private void retro16$skipFeatures(ChunkRegion region, CallbackInfo ci) {
		if (Retro16.isChunkOutside(this.world, region.getCenterChunkX(), region.getCenterChunkZ())) {
			ci.cancel();
		}
	}

	@Inject(method = "carve", at = @At("HEAD"), cancellable = true)
	private void retro16$skipCarving(Chunk chunk, GenerationStep.Carver carver, CallbackInfo ci) {
		if (Retro16.isChunkOutside(this.world, chunk.getPos().x, chunk.getPos().z)) {
			ci.cancel();
		}
	}
}
