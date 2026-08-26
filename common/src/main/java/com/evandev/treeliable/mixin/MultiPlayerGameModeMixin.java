package com.evandev.treeliable.mixin;

import com.evandev.treeliable.client.Client;
import com.evandev.treeliable.client.SpiderwebVisualizer;
import com.evandev.treeliable.common.chop.ChopUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    @Final
    private ClientPacketListener connection;

    @Shadow
    private BlockPos destroyBlockPos;

    @Shadow
    private float destroyProgress;

    @Shadow
    private boolean isDestroying;

    @Unique
    private BlockPos treeliable$trackedPos = null;

    @Unique
    private Boolean treeliable$trackedChopState = null;

    @Inject(method = "tick", at = @At("TAIL"))
    public void treeliable$tickSpiderweb(CallbackInfo ci) {
        SpiderwebVisualizer.update(this.minecraft, this.isDestroying, this.destroyBlockPos, this.destroyProgress);
    }

    @Inject(method = "continueDestroyBlock", at = @At("HEAD"))
    private void treeliable$resetProgressOnChopStateChange(BlockPos posBlock, Direction directionFacing, CallbackInfoReturnable<Boolean> cir) {
        if (this.minecraft.player == null) {
            return;
        }

        boolean currentChopState = ChopUtil.playerWantsToChop(this.minecraft.player, Client.getChopSettings());
        boolean samePos = posBlock.equals(this.treeliable$trackedPos) && posBlock.equals(this.destroyBlockPos);

        if (samePos && this.treeliable$trackedChopState != null && this.treeliable$trackedChopState != currentChopState) {
            this.destroyProgress = 0.0F;
            this.connection.send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK, posBlock, directionFacing));
        }

        this.treeliable$trackedPos = posBlock;
        this.treeliable$trackedChopState = currentChopState;
    }
}
