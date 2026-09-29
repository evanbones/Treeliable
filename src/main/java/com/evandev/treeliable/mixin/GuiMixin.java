package com.evandev.treeliable.mixin;

import com.evandev.treeliable.Treeliable;
import com.evandev.treeliable.client.SpiderwebVisualizer;
import com.evandev.treeliable.common.config.ModConfig;
//? if <26.1 {
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
//?}
//? if >=1.21
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
//? if >=26.2 {
//import net.minecraft.client.gui.Hud;
//?} else
import net.minecraft.client.gui.Gui;
//? if >=26.1 {
/*import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
*///?} else
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=26.2 {
//@Mixin(Hud.class)
//?} else
@Mixin(Gui.class)
public class GuiMixin {
    @Unique
    private static final ResourceLocation CHOP_ICON = Treeliable.resource("textures/gui/chop_icon.png");

    //? if >=26.1 {
    /*@Inject(method = "extractCrosshair", at = @At("TAIL"))
    private void treeliable$renderChopIcon(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
    *///?} else {
    @Inject(method = "renderCrosshair", at = @At("TAIL"))
    private void treeliable$renderChopIcon(GuiGraphics guiGraphics, /*? if >=1.21 {*/DeltaTracker deltaTracker,/*?}*/ CallbackInfo ci) {
    //?}
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.hitResult != null && minecraft.hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) minecraft.hitResult;

            try {
                if (ModConfig.get().showChoppingIndicator && SpiderwebVisualizer.blockCanBeChopped(blockHit.getBlockPos())) {
                    int screenWidth = guiGraphics.guiWidth();
                    int screenHeight = guiGraphics.guiHeight();

                    int x = (screenWidth / 2) + ModConfig.get().choppingIndicatorXOffset;
                    int y = (screenHeight / 2) + ModConfig.get().choppingIndicatorYOffset;

                    //? if >=26.1 {
                    /*guiGraphics.blit(
                            RenderPipelines.CROSSHAIR,
                            CHOP_ICON,
                            x,
                            y,
                            0.0F,
                            0.0F,
                            16,
                            16,
                            16,
                            16,
                            -1
                    );
                    *///?} else {
                    RenderSystem.enableBlend();

                    RenderSystem.blendFuncSeparate(
                            GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR,
                            GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,
                            GlStateManager.SourceFactor.ONE,
                            GlStateManager.DestFactor.ZERO
                    );

                    guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
                    guiGraphics.blit(CHOP_ICON, x, y, 0, 0, 16, 16, 16, 16);

                    RenderSystem.defaultBlendFunc();
                    RenderSystem.disableBlend();
                    //?}
                }
            } catch (Exception ignored) {
            }
        }
    }
}
