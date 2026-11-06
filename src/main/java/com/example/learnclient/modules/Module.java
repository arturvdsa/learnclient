package com.example.learnclient.modules;

import net.minecraft.client.MinecraftClient;

/**
 * Базовый класс модуля: имя, категория, состояние вкл/выкл и хуки.
 */
public abstract class Module {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();

    public enum Category {
        COMBAT("Combat"),
        MOVEMENT("Movement"),
        RENDER("Render"),
        PLAYER("Player"),
        MISC("Misc");

        public final String title;

        Category(String title) {
            this.title = title;
        }
    }

    private final String name;
    private final Category category;
    private boolean enabled;

    protected Module(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;
        if (enabled) onEnable(); else onDisable();
    }

    protected void onEnable() {}

    protected void onDisable() {}

    /** Каждый тик клиента, пока модуль включён и игрок в мире. */
    public void onTick() {}
}
