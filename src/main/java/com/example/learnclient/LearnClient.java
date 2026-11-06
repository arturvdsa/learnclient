package com.example.learnclient;

import com.example.learnclient.modules.Module;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LearnClient implements ClientModInitializer {
    public static final ModuleManager MODULES = new ModuleManager();

    private static KeyBinding openGuiKey;

    @Override
    public void onInitializeClient() {
        MODULES.init();

        // Открытие ClickGUI на правый Shift (клавишу можно сменить в настройках управления)
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.learnclient.gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                KeyBinding.Category.create(Identifier.of("learnclient", "main"))));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                if (client.currentScreen == null && client.player != null) {
                    client.setScreen(new ClickGuiScreen());
                }
            }
            if (client.player != null) MODULES.tick();
        });

        // Список включённых модулей справа сверху (ArrayList), по длине названия
        HudRenderCallback.EVENT.register((context, tickCounter) -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player == null || mc.options.hudHidden) return;

            List<Module> active = new ArrayList<>();
            for (Module m : MODULES.getModules()) {
                if (m.isEnabled()) active.add(m);
            }
            active.sort(Comparator.comparingInt((Module m) -> mc.textRenderer.getWidth(m.getName())).reversed());

            int screenW = context.getScaledWindowWidth();
            int y = 4;
            for (Module m : active) {
                int w = mc.textRenderer.getWidth(m.getName());
                int x = screenW - w - 6;
                context.fill(x - 3, y - 1, screenW, y + 10, 0x90000000);
                context.fill(screenW - 2, y - 1, screenW, y + 10, 0xFF8F7BFF);
                context.drawTextWithShadow(mc.textRenderer, m.getName(), x, y, 0xFF8F7BFF);
                y += 11;
            }
        });
    }
}
