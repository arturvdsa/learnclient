package com.example.learnclient.modules;

/**
 * HUD: включает/выключает ватермарк и список модулей (ArrayList).
 * Включён по умолчанию.
 */
public class HudModule extends Module {
    public HudModule() {
        super("HUD", Category.RENDER);
        setEnabled(true);
    }
}
