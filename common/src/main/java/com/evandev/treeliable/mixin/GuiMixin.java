package com.evandev.treeliable.mixin;

import com.evandev.treeliable.Treeliable;
import com.evandev.treeliable.client.SpiderwebVisualizer;
import com.evandev.treeliable.common.config.ModConfig;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
    private static final Identifier CHOP_ICON = Treeliable.resource("textures/gui/chop_icon.png");

    @Inject(method = "extractCrosshair", at = @At("TAIL"))
    private void treeliable$renderChopIcon(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.hitResult != null && minecraft.hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) minecraft.hitResult;

            try {
                if (ModConfig.get().showChoppingIndicator && SpiderwebVisualizer.blockCanBeChopped(blockHit.getBlockPos())) {
                    int screenWidth = graphics.guiWidth();
                    int screenHeight = graphics.guiHeight();

                    int x = (screenWidth / 2) + ModConfig.get().choppingIndicatorXOffset;
                    int y = (screenHeight / 2) + ModConfig.get().choppingIndicatorYOffset;

                    graphics.blit(
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
                }
            } catch (Exception ignored) {
            }
        }
    }
}