package com.example.learnclient.modules;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

/**
 * ESP: подсвечивает существа контуром (клиентский эффект свечения).
 */
public class EspModule extends Module {
    public EspModule() {
        super("Entity ESP", Category.RENDER);
    }

    @Override
    public void onTick() {
        if (mc.world == null) return;
        for (Entity e : mc.world.getEntities()) {
            if (e instanceof LivingEntity && e != mc.player) {
                e.setGlowing(true);
            }
        }
    }

    @Override
    protected void onDisable() {
        if (mc.world == null) return;
        for (Entity e : mc.world.getEntities()) {
            if (e != mc.player) e.setGlowing(false);
        }
    }
}
