package com.example.learnclient.modules;

/**
 * Auto Respawn: автоматически возрождает игрока после смерти.
 */
public class AutoRespawnModule extends Module {
    public AutoRespawnModule() {
        super("Auto Respawn", Category.PLAYER);
    }

    @Override
    public void onTick() {
        if (mc.player != null && mc.player.isDead()) {
            mc.player.requestRespawn();
        }
    }
}
