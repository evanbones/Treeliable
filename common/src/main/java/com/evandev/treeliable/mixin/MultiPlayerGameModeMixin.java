package com.evandev.treeliable.mixin;

import com.evandev.treeliable.client.SpiderwebVisualizer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin {
    @Shadow()
    @Final
    private Minecraft minecraft;
    @Shadow
    private BlockPos destroyBlockPos;
    @Shadow
    private float destroyProgress;
    @Shadow
    private boolean isDestroying;

    @Shadow()
    public abstract boolean isServerControlledInventory();

    @Inject(method = "tick", at = @At("TAIL"))
    public void treeliable$tickSpiderweb(CallbackInfo ci) {
        SpiderwebVisualizer.update(this.minecraft, this.isDestroying, this.destroyBlockPos, this.destroyProgress);
    }
}