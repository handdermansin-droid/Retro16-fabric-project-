package com.example.retro16.mixin;

import com.example.retro16.Retro16;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Tick/entity side of the 2000x2000 border: chunks outside the square receive no random
 * ticks, and non-player entities outside it are not processed.
 */
@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {

	@Inject(method = "tickChunk", at = @At("HEAD"), cancellable = true)
	private void retro16$skipChunkTicks(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci) {
		ServerWorld self = (ServerWorld) (Object) this;
		if (Retro16.isChunkOutside(self, chunk.getPos().x, chunk.getPos().z)) {
			ci.cancel();
		}
	}

	@Inject(method = "tickEntity", at = @At("HEAD"), cancellable = true)
	private void retro16$skipEntityTicks(Entity entity, CallbackInfo ci) {
		if (entity instanceof PlayerEntity) {
			return;
		}
		ServerWorld self = (ServerWorld) (Object) this;
		BlockPos pos = entity.getBlockPos();
		if (Retro16.isBlockOutside(self, pos.getX(), pos.getZ())) {
			if (Retro16.REMOVE_OUT_OF_BORDER_ENTITIES) {
				entity.remove();
			}
			ci.cancel();
		}
	}
}
