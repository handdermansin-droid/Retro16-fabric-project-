package com.example.retro16.mixin.client;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlFramebuffer;
import net.minecraft.client.options.AoOption;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.render.GameRenderer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.ByteBuffer;

/**
 * Every frame, before rendering:
 *  1. forces Smooth Lighting (ambient occlusion) to OFF;
 *  2. makes sure the main framebuffer's color texture uses a 16-bit internal format
 *     (GL_RGB5_A1). Vanilla re-creates that texture as RGBA8 on every resize, so the
 *     format is re-applied whenever the texture id or size changes.
 */
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

	@Shadow
	@Final
	private MinecraftClient client;

	private int retro16$lastColorTexture = -1;
	private int retro16$lastWidth = -1;
	private int retro16$lastHeight = -1;

	@Inject(method = "render", at = @At("HEAD"))
	private void retro16$force16BitFlatLighting(float tickDelta, long nanoTime, boolean tick, CallbackInfo ci) {
		GameOptions options = this.client.options;
		if (options.ao != AoOption.OFF) {
			options.ao = AoOption.OFF;
		}

		GlFramebuffer framebuffer = this.client.getFramebuffer();
		if (framebuffer == null || framebuffer.colorAttachment <= 0) {
			return;
		}

		if (framebuffer.colorAttachment != this.retro16$lastColorTexture
				|| framebuffer.texWidth != this.retro16$lastWidth
				|| framebuffer.texHeight != this.retro16$lastHeight) {

			GlStateManager.bindTexture(framebuffer.colorAttachment);
			GL11.glTexImage2D(
					GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB5_A1,
					framebuffer.texWidth, framebuffer.texHeight, 0,
					GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
			GlStateManager.bindTexture(0);

			this.retro16$lastColorTexture = framebuffer.colorAttachment;
			this.retro16$lastWidth = framebuffer.texWidth;
			this.retro16$lastHeight = framebuffer.texHeight;
		}
	}
}
