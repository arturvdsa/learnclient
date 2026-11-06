package com.example.learnclient;

import com.example.learnclient.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * ClickGUI в тёмно-красном стиле: 5 панелей, переключатели справа, поиск по модулям снизу.
 * ЛКМ по модулю включает/выключает, ПКМ по заголовку сворачивает панель,
 * заголовки перетаскиваются. Ввод опрашивается через GLFW, чтобы код не зависел
 * от сигнатур методов Screen, которые менялись между версиями.
 */
public class ClickGuiScreen extends Screen {
    private static final int PANEL_W = 100;
    private static final int GAP = 6;
    private static final int HEADER_H = 20;
    private static final int ROW_H = 15;

    private static final int PANEL_TOP = 0xE0801818;
    private static final int PANEL_BOTTOM = 0xE0420A0A;
    private static final int HEADER_TOP = 0xF0A02020;
    private static final int HEADER_BOTTOM = 0xF0701515;
    private static final int ROW_ACTIVE = 0x30FF3030;
    private static final int ROW_HOVER = 0x25FFFFFF;
    private static final int SWITCH_ON = 0xFFFF3B3B;
    private static final int SWITCH_OFF = 0xFF55302F;
    private static final int TEXT_ON = 0xFFFFFFFF;
    private static final int TEXT_OFF = 0xFFE0C8C8;

    private static class Panel {
        int x, y;
        boolean open = true;
    }

    private final Map<Module.Category, Panel> panels = new EnumMap<>(Module.Category.class);
    private boolean laidOut;

    private boolean prevLeft, prevRight, prevShift;
    private final boolean[] prevKeys = new boolean[GLFW.GLFW_KEY_LAST + 1];
    private Module.Category dragging;
    private int dragDx, dragDy;
    private final StringBuilder search = new StringBuilder();

    public ClickGuiScreen() {
        super(Text.literal("LearnClient"));
        for (Module.Category c : Module.Category.values()) panels.put(c, new Panel());

        // Запоминаем текущее состояние, чтобы нажатие, открывшее GUI, не считалось вводом
        long w = MinecraftClient.getInstance().getWindow().getHandle();
        prevLeft = GLFW.glfwGetMouseButton(w, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
        prevRight = GLFW.glfwGetMouseButton(w, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;
        prevShift = true;
        for (int k = 0; k < prevKeys.length; k++) {
            prevKeys[k] = GLFW.glfwGetKey(w, k) == GLFW.GLFW_PRESS;
        }
    }

    @Override
    protected void init() {
        if (laidOut) return;
        laidOut = true;
        int count = Module.Category.values().length;
        int total = count * PANEL_W + (count - 1) * GAP;
        int x = Math.max(4, (width - total) / 2);
        for (Module.Category c : Module.Category.values()) {
            Panel p = panels.get(c);
            p.x = x;
            p.y = Math.max(10, height / 2 - 110);
            x += PANEL_W + GAP;
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private boolean pressedNow(long window, int key) {
        boolean now = GLFW.glfwGetKey(window, key) == GLFW.GLFW_PRESS;
        boolean fired = now && !prevKeys[key];
        prevKeys[key] = now;
        return fired;
    }

    private void handleTyping(long window) {
        for (int k = GLFW.GLFW_KEY_A; k <= GLFW.GLFW_KEY_Z; k++) {
            if (pressedNow(window, k) && search.length() < 20) {
                search.append((char) ('a' + (k - GLFW.GLFW_KEY_A)));
            }
        }
        if (pressedNow(window, GLFW.GLFW_KEY_SPACE) && search.length() < 20) search.append(' ');
        if (pressedNow(window, GLFW.GLFW_KEY_BACKSPACE) && search.length() > 0) {
            search.deleteCharAt(search.length() - 1);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        long w = client.getWindow().getHandle();
        boolean left = GLFW.glfwGetMouseButton(w, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
        boolean right = GLFW.glfwGetMouseButton(w, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;
        boolean shift = GLFW.glfwGetKey(w, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;

        // Правый Shift закрывает GUI
        if (shift && !prevShift) {
            close();
            return;
        }
        prevShift = shift;
        prevKeys[GLFW.GLFW_KEY_RIGHT_SHIFT] = shift;

        handleTyping(w);

        boolean leftClick = left && !prevLeft;
        boolean rightClick = right && !prevRight;
        prevLeft = left;
        prevRight = right;

        context.fill(0, 0, width, height, 0x70000000);

        if (dragging != null) {
            if (left) {
                Panel p = panels.get(dragging);
                p.x = mouseX - dragDx;
                p.y = mouseY - dragDy;
            } else {
                dragging = null;
            }
        }

        String query = search.toString().trim().toLowerCase();

        for (Module.Category category : Module.Category.values()) {
            Panel p = panels.get(category);

            List<Module> list = new ArrayList<>();
            for (Module m : LearnClient.MODULES.getModules(category)) {
                if (query.isEmpty() || m.getName().toLowerCase().contains(query)) list.add(m);
            }

            int bodyH = p.open ? list.size() * ROW_H + 4 : 0;
            roundedGradient(context, p.x, p.y, p.x + PANEL_W, p.y + HEADER_H + bodyH, PANEL_TOP, PANEL_BOTTOM);
            roundedGradient(context, p.x, p.y, p.x + PANEL_W, p.y + HEADER_H, HEADER_TOP, HEADER_BOTTOM);
            context.drawTextWithShadow(textRenderer, category.title, p.x + 8, p.y + 6, 0xFFFFFFFF);

            boolean overHeader = inside(mouseX, mouseY, p.x, p.y, PANEL_W, HEADER_H);
            if (overHeader && leftClick && dragging == null) {
                dragging = category;
                dragDx = mouseX - p.x;
                dragDy = mouseY - p.y;
            }
            if (overHeader && rightClick) p.open = !p.open;

            if (!p.open) continue;

            int y = p.y + HEADER_H + 2;
            for (Module m : list) {
                boolean hover = inside(mouseX, mouseY, p.x + 3, y, PANEL_W - 6, ROW_H);
                if (m.isEnabled()) rounded(context, p.x + 3, y, p.x + PANEL_W - 3, y + ROW_H, ROW_ACTIVE);
                if (hover) rounded(context, p.x + 3, y, p.x + PANEL_W - 3, y + ROW_H, ROW_HOVER);

                context.drawTextWithShadow(textRenderer, m.getName(), p.x + 9, y + 4,
                        m.isEnabled() ? TEXT_ON : TEXT_OFF);
                drawSwitch(context, p.x + PANEL_W - 24, y + 4, m.isEnabled());

                if (hover && leftClick && dragging == null) m.toggle();
                y += ROW_H;
            }
        }

        // Поле поиска снизу по центру
        int sw = 150, sh = 18;
        int sx = (width - sw) / 2;
        int sy = height - 34;
        rounded(context, sx, sy, sx + sw, sy + sh, 0xD0200808);
        if (search.length() == 0) {
            context.drawTextWithShadow(textRenderer, "Поиск по модулям", sx + 8, sy + 5, 0xFF8A6A6A);
        } else {
            context.drawTextWithShadow(textRenderer, search.toString(), sx + 8, sy + 5, 0xFFFFFFFF);
        }
    }

    /** Переключатель-«таблетка» с кружком. */
    private void drawSwitch(DrawContext c, int x, int y, boolean on) {
        rounded(c, x, y, x + 18, y + 8, on ? SWITCH_ON : SWITCH_OFF);
        int kx = on ? x + 11 : x + 1;
        c.fill(kx, y + 1, kx + 6, y + 7, 0xFFFFFFFF);
    }

    /** Прямоугольник со срезанными углами (имитация скругления). */
    private static void rounded(DrawContext c, int x1, int y1, int x2, int y2, int color) {
        c.fill(x1 + 2, y1, x2 - 2, y2, color);
        c.fill(x1, y1 + 2, x2, y2 - 2, color);
        c.fill(x1 + 1, y1 + 1, x2 - 1, y2 - 1, color);
    }

    private static void roundedGradient(DrawContext c, int x1, int y1, int x2, int y2, int top, int bottom) {
        c.fillGradient(x1 + 1, y1, x2 - 1, y2, top, bottom);
        c.fillGradient(x1, y1 + 2, x2, y2 - 2, top, bottom);
    }

    private static boolean inside(int mx, int my, int x, int y, int w, int h) {
        return mx >= x && mx < x + w && my >= y && my < y + h;
    }
}
