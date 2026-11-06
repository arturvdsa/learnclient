package com.example.learnclient.modules;

import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;

/**
 * Step: позволяет подниматься на блоки высотой до 1 без прыжка.
 */
public class StepModule extends Module {
    private static final double DEFAULT_HEIGHT = 0.6;

    public StepModule() {
        super("Step", Category.MOVEMENT);
    }

    private void setHeight(double value) {
        if (mc.player == null) return;
        EntityAttributeInstance attr = mc.player.getAttributeInstance(EntityAttributes.STEP_HEIGHT);
        if (attr != null) attr.setBaseValue(value);
    }

    @Override
    protected void onEnable() {
        setHeight(1.0);
    }

    @Override
    protected void onDisable() {
        setHeight(DEFAULT_HEIGHT);
    }
}
