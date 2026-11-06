package com.example.learnclient;

import com.example.learnclient.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.Comparator;
import java.util.List;

/**
 * Отрисовка HUD: ватермарк и список включённых модулей справа сверху.
 */
public class HudRenderer {
    public static void render(DrawContext ctx) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.options.hudHidden) return;
        if (!LearnClient.MODULES.isHudEnabled()) return;

        ctx.drawTextWithShadow(mc.textRenderer, "LearnClient", 4, 4, ClickGuiScreen.ACCENT);

        List<Module> active = LearnClient.MODULES.getModules().stream()
                .filter(Module::isEnabled)
                .filter(m -> !m.getName().equals("HUD"))
                .sorted(Comparator.comparingInt((Module m) -> mc.textRenderer.getWidth(m.getName())).reversed())
                .toList();

        int screenW = ctx.getScaledWindowWidth();
        int y = 2;
        for (Module m : active) {
            int w = mc.textRenderer.getWidth(m.getName());
            int x = screenW - w - 6;
            ctx.fill(x - 2, y, screenW, y + 10, 0x90000000);
            ctx.fill(screenW - 2, y, screenW, y + 10, ClickGuiScreen.ACCENT);
            ctx.drawTextWithShadow(mc.textRenderer, m.getName(), x, y + 1, 0xFFFFFFFF);
            y += 10;
        }
    }
}
