package com.example.learnclient;

import com.example.learnclient.modules.Category;
import com.example.learnclient.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.List;

/**
 * ClickGUI в стиле хак-клиентов: панель на каждую категорию,
 * клик ЛКМ по модулю включает/выключает его.
 * Клики определяются опросом GLFW, чтобы не зависеть от сигнатур событий мыши.
 */
public class ClickGuiScreen extends Screen {
    public static final int ACCENT = 0xFF6C7CFF;
    private static final int PANEL_W = 110;
    private static final int HEADER_H = 16;
    private static final int ROW_H = 14;

    private boolean wasDown;

    public ClickGuiScreen() {
        super(Text.literal("ClickGUI"));
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ctx.fill(0, 0, width, height, 0x80000000);

        boolean down = GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT)
                == GLFW.GLFW_PRESS;
        boolean clicked = down && !wasDown;
        wasDown = down;

        int x = 20;
        for (Category cat : Category.values()) {
            List<Module> mods = LearnClient.MODULES.getModules().stream()
                    .filter(m -> m.getCategory() == cat).toList();

            int panelH = HEADER_H + mods.size() * ROW_H;
            ctx.fill(x, 20, x + PANEL_W, 20 + panelH, 0xE0161620);
            ctx.fill(x, 20, x + PANEL_W, 20 + HEADER_H, ACCENT);
            ctx.drawTextWithShadow(mc.textRenderer, cat.title, x + 5, 24, 0xFFFFFFFF);

            int y = 20 + HEADER_H;
            for (Module m : mods) {
                boolean hover = mouseX >= x && mouseX < x + PANEL_W && mouseY >= y && mouseY < y + ROW_H;
                if (hover) ctx.fill(x, y, x + PANEL_W, y + ROW_H, 0x30FFFFFF);
                if (hover && clicked) m.toggle();

                int color = m.isEnabled() ? ACCENT : 0xFFBBBBBB;
                ctx.drawTextWithShadow(mc.textRenderer, m.getName(), x + 5, y + 3, color);
                y += ROW_H;
            }
            x += PANEL_W + 10;
        }
        super.render(ctx, mouseX, mouseY, delta);
    }
}
