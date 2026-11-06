package com.example.learnclient.modules;

import net.minecraft.util.math.Vec3d;

/**
 * Полёт: Пробел вверх, Shift вниз, иначе зависание на месте.
 */
public class FlyModule extends Module {
    private static final double VERTICAL_SPEED = 0.35;

    public FlyModule() {
        super("Fly", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;

        double y = 0;
        if (mc.options.jumpKey.isPressed()) y += VERTICAL_SPEED;
        if (mc.options.sneakKey.isPressed()) y -= VERTICAL_SPEED;

        Vec3d v = mc.player.getVelocity();
        mc.player.setVelocity(v.x, y, v.z);
        mc.player.fallDistance = 0;
    }
}
