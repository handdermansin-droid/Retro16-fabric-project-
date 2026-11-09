package com.example.retro16.mixin;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Removes the upper clamp (max health 1024, speed 1024, horse jump 2.0) for the three
 * horse-relevant attributes. The lower clamp is kept. All other attributes are untouched.
 */
@Mixin(ClampedEntityAttribute.class)
public abstract class ClampedEntityAttributeMixin {

	@Shadow
	@Final
	private double minValue;

	@Inject(method = "clamp", at = @At("HEAD"), cancellable = true)
	private void retro16$stripUpperClamp(double value, CallbackInfoReturnable<Double> cir) {
		String id = ((EntityAttribute) (Object) this).getId();
		if ("generic.maxHealth".equals(id)
				|| "generic.movementSpeed".equals(id)
				|| "horse.jumpStrength".equals(id)) {
			cir.setReturnValue(Math.max(value, this.minValue));
		}
	}
}
