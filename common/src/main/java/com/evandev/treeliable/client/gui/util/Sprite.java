package com.evandev.treeliable.client.gui.util;

import com.evandev.treeliable.Treeliable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public enum Sprite {
    CHOP_INDICATOR(0, 0, 20, 20),
    NO_FELL_INDICATOR(20, 0, 20, 20),
    INDICATOR_ERROR(60, 0, 12, 12),
    INDICATOR_WARNING(72, 0, 12, 12),
    ;

    public static final ResourceLocation TEXTURE_PATH =
            ResourceLocation.fromNamespaceAndPath(Treeliable.MOD_ID, "textures/gui/widgets.png");
    public static final int TEXTURE_WIDTH = 64;
    public static final int TEXTURE_HEIGHT = 120;
    public final int width;
    public final int height;
    private final int u;
    private final int v;

    Sprite(int u, int v, int width, int height) {
        this.u = u;
        this.v = v;
        this.width = width;
        this.height = height;
    }

    public void blit(GuiGraphics gui, int x, int y, int width, int height, boolean mirror) {
        float u = mirror ? this.u + this.width : this.u;
        int uw = mirror ? -this.width : this.width;
        gui.blit(TEXTURE_PATH, x, y, width, height, u, v, uw, this.height, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }
}
