package com.example.retro16.mixin;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.PassiveEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Replaces vanilla's (parentA + parentB + random bonus) / 3 inheritance with:
 *   child = max(parentA, parentB) * 1.10
 * for Max Health, Movement Speed and Jump Strength.
 * Upper clamps are removed separately in ClampedEntityAttributeMixin.
 */
@Mixin(HorseBaseEntity.class)
public abstract class HorseBaseEntityMixin {

	private static final double GENERATIONAL_BONUS = 1.10D;

	@Inject(method = "setChildAttributes", at = @At("HEAD"), cancellable = true)
	private void retro16$stackAttributes(PassiveEntity mate, HorseBaseEntity child, CallbackInfo ci) {
		HorseBaseEntity self = (HorseBaseEntity) (Object) this;

		double health = retro16$bestOf(self, mate, EntityAttributes.MAX_HEALTH) * GENERATIONAL_BONUS;
		double jump = retro16$bestOf(self, mate, HorseBaseEntity.JUMP_STRENGTH) * GENERATIONAL_BONUS;
		double speed = retro16$bestOf(self, mate, EntityAttributes.MOVEMENT_SPEED) * GENERATIONAL_BONUS;

		child.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(health);
		child.getAttributeInstance(HorseBaseEntity.JUMP_STRENGTH).setBaseValue(jump);
		child.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(speed);

		// New max health would otherwise leave the foal at its spawn health.
		child.setHealth(child.getHealthMaximum());

		// Skip the vanilla averaging entirely.
		ci.cancel();
	}

	private static double retro16$bestOf(HorseBaseEntity a, PassiveEntity b, EntityAttribute attribute) {
		EntityAttributeInstance ia = a.getAttributeInstance(attribute);
		EntityAttributeInstance ib = b.getAttributeInstance(attribute);
		double va = ia != null ? ia.getBaseValue() : 0.0D;
		double vb = ib != null ? ib.getBaseValue() : 0.0D;
		return Math.max(va, vb);
	}
}
