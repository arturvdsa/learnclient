package com.example.learnclient.modules;

/**
 * Автоспринт: пока игрок идёт вперёд, спринт включается сам.
 */
public class SprintModule extends Module {
    public SprintModule() {
        super("Sprint", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (mc.options.forwardKey.isPressed() && !mc.player.isSneaking()) {
            mc.player.setSprinting(true);
        }
    }
}
