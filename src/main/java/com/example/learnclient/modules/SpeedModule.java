package com.example.learnclient.modules;

import net.minecraft.util.math.Vec3d;

/**
 * Speed: ускоряет горизонтальное движение.
 */
public class SpeedModule extends Module {
    private static final double SPEED = 0.32;

    public SpeedModule() {
        super("Speed", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;

        float forward = (mc.options.forwardKey.isPressed() ? 1 : 0) - (mc.options.backKey.isPressed() ? 1 : 0);
        float strafe = (mc.options.leftKey.isPressed() ? 1 : 0) - (mc.options.rightKey.isPressed() ? 1 : 0);
        if (forward == 0 && strafe == 0) return;

        double yaw = Math.toRadians(mc.player.getYaw());
        double dx = -Math.sin(yaw) * forward + Math.cos(yaw) * strafe;
        double dz = Math.cos(yaw) * forward + Math.sin(yaw) * strafe;
        double len = Math.sqrt(dx * dx + dz * dz);
        if (len == 0) return;

        Vec3d v = mc.player.getVelocity();
        mc.player.setVelocity(dx / len * SPEED, v.y, dz / len * SPEED);
    }
}
