package com.example.learnclient.modules;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

/**
 * Fullbright: клиентское ночное зрение, чтобы видеть в темноте.
 */
public class FullbrightModule extends Module {
    public FullbrightModule() {
        super("Full Bright", Category.RENDER);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        mc.player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.NIGHT_VISION, 400, 0, false, false, false));
    }

    @Override
    protected void onDisable() {
        if (mc.player != null) {
            mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
        }
    }
}
