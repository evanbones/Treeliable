package com.evandev.treeliable.mixin;

import com.evandev.treeliable.api.TreeData;
import com.evandev.treeliable.client.Client;
import com.evandev.treeliable.common.chop.ChopUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateMixin {

    @Inject(method = "getDestroyProgress", at = @At("RETURN"), cancellable = true)
    public void treeliable$scaleDestroyProgress(Player player, BlockGetter blockGetter, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        if (blockGetter instanceof Level level && ChopUtil.isBlockChoppable(level, pos)) {
            TreeData tree = Client.treeCache.getTree(level, pos);

            if (tree != null && tree.isAProperTree(true)) {
                int chopsNeeded = tree.numChopsNeededToFell();
                if (chopsNeeded > 1) {
                    cir.setReturnValue(cir.getReturnValue() / (float) chopsNeeded);
                }
            }
        }
    }
}