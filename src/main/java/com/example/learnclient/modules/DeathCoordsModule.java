package com.example.learnclient.modules;

import net.minecraft.text.Text;

/**
 * Death Coords: пишет в чат координаты места смерти.
 */
public class DeathCoordsModule extends Module {
    private boolean wasDead;

    public DeathCoordsModule() {
        super("Death Coords", Category.MISC);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        boolean dead = mc.player.isDead();
        if (dead && !wasDead) {
            mc.player.sendMessage(Text.literal(String.format("Вы умерли: x %d y %d z %d",
                    (int) mc.player.getX(), (int) mc.player.getY(), (int) mc.player.getZ())), false);
        }
        wasDead = dead;
    }
}
