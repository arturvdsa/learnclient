package com.example.learnclient.modules;

import net.minecraft.util.math.Vec3d;

/**
 * Speed: небольшое ускорение при ходьбе по земле.
 */
public class SpeedModule extends Module {
    private static final double MULTIPLIER = 1.08;
    private static final double MAX_HORIZONTAL = 0.45;

    public SpeedModule() {
        super("Speed", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player == null || !mc.player.isOnGround()) return;
        if (!mc.player.input.hasForwardMovement()) return;

        Vec3d v = mc.player.getVelocity();
        double h = Math.hypot(v.x, v.z);
        if (h < 0.0001 || h > MAX_HORIZONTAL) return;
        mc.player.setVelocity(v.x * MULTIPLIER, v.y, v.z * MULTIPLIER);
    }
}
