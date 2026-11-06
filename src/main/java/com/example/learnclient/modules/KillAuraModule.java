package com.example.learnclient.modules;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;

/**
 * KillAura: атакует ближайшее существо в радиусе (только одиночная игра для тестов).
 */
public class KillAuraModule extends Module {
    private static final double RANGE = 4.0;

    public KillAuraModule() {
        super("Aura", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;
        if (mc.player.getAttackCooldownProgress(0.5f) < 1.0f) return;

        LivingEntity best = null;
        double bestDist = RANGE * RANGE;
        for (Entity e : mc.world.getEntities()) {
            if (!(e instanceof LivingEntity living) || e == mc.player || !living.isAlive()) continue;
            double d = mc.player.squaredDistanceTo(e);
            if (d < bestDist) {
                bestDist = d;
                best = living;
            }
        }

        if (best != null) {
            mc.interactionManager.attackEntity(mc.player, best);
            mc.player.swingHand(Hand.MAIN_HAND);
        }
    }
}
