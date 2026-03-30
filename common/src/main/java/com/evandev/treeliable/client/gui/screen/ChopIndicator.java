package com.evandev.treeliable.client.gui.screen;

import com.evandev.treeliable.Treeliable;
import com.evandev.treeliable.TreeliableException;
import com.evandev.treeliable.client.Client;
import com.evandev.treeliable.client.gui.util.Sprite;
import com.evandev.treeliable.client.settings.ClientChopSettings;
import com.evandev.treeliable.common.chop.ChopUtil;
import com.evandev.treeliable.common.config.ModConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ChopIndicator {

    private static final double IMAGE_SCALE = 1.0;

    public static void render(GuiGraphics gui, int windowWidth, int windowHeight) {
        Minecraft minecraft = Minecraft.getInstance();
        HitResult mouseOver = minecraft.hitResult;
        Player player = minecraft.player;

        try {
            if (Client.isChoppingIndicatorEnabled() &&
                    player != null && !player.isSpectator() &&
                    minecraft.level != null && minecraft.screen == null && mouseOver != null &&
                    mouseOver.getType() == HitResult.Type.BLOCK && mouseOver instanceof BlockHitResult &&
                    ChopUtil.playerWantsToChop(player, Client.getChopSettings())
            ) {
                BlockPos blockPos = ((BlockHitResult) mouseOver).getBlockPos();
                if (blockCanBeChopped(blockPos)) {
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();

                    boolean mirror = player.getMainArm() == HumanoidArm.LEFT;
                    int indicatorCenterX = windowWidth / 2 + ModConfig.get().indicatorXOffset * (mirror ? -1 : 1);
                    int indicatorCenterY = windowHeight / 2 + ModConfig.get().indicatorYOffset;

                    Sprite sprite = Sprite.CHOP_INDICATOR;
                    int imageWidth = (int) (sprite.width * IMAGE_SCALE);
                    int imageHeight = (int) (sprite.height * IMAGE_SCALE);

                    sprite.blit(
                            gui,
                            indicatorCenterX - imageWidth / 2,
                            indicatorCenterY - imageHeight / 2,
                            imageWidth,
                            imageHeight,
                            mirror
                    );

                    RenderSystem.disableBlend();
                }
            }
        } catch (Exception e) {
            Treeliable.cry(e);
        }
    }

    public static boolean blockCanBeChopped(BlockPos pos) throws TreeliableException {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        ClientLevel level = minecraft.level;
        ClientChopSettings chopSettings = Client.getChopSettings();

        if (player == null || level == null) {
            return false;
        }

        boolean wantToChop = ChopUtil.canChopWithTool(player, level, pos) && ChopUtil.playerWantsToChop(minecraft.player, chopSettings);
        if (wantToChop) {
            return isAProperTree(pos, level, chopSettings);
        }

        return false;
    }

    private static boolean isAProperTree(BlockPos pos, ClientLevel level, ClientChopSettings chopSettings) throws TreeliableException {
        try {
            return Client.treeCache.getTree(level, pos).isAProperTree(chopSettings.getTreesMustHaveLeaves());
        } catch (Exception e) {
            throw new TreeliableException(String.format("Parameters: %s, %s, %s", pos, level, chopSettings), e);
        }
    }
}