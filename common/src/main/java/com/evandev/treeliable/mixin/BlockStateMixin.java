package com.evandev.treeliable.mixin;

import com.evandev.treeliable.api.TreeData;
import com.evandev.treeliable.client.Client;
import com.evandev.treeliable.common.chop.ChopUtil;
import com.evandev.treeliable.common.config.ModConfig;
import com.evandev.treeliable.common.settings.ChopSettings;
import com.evandev.treeliable.platform.server.Server;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateMixin {

    @Inject(method = "getDestroyProgress", at = @At("RETURN"), cancellable = true)
    public void treeliable$scaleDestroyProgress(Player player, BlockGetter blockGetter, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        if (blockGetter instanceof Level level && ChopUtil.isBlockChoppable(level, pos)) {
            BlockState state = blockGetter.getBlockState(pos);

            ChopSettings settings;
            if (level instanceof ServerLevel) {
                settings = Server.instance().getPlayerChopData(player).getSettings();
            } else {
                settings = Client.getChopSettings();
            }

            if (!ChopUtil.playerWantsToChop(player, settings)) {
                return;
            }

            if (!ChopUtil.canChopWithTool(player, player.getMainHandItem(), level, pos, state)) {
                return;
            }

            TreeData tree;
            if (level instanceof ServerLevel) {
                tree = ChopUtil.getTree(level, pos);
            } else {
                tree = Client.treeCache.getTree(level, pos);
            }

            if (tree != null && tree.isAProperTree(settings.getTreesMustHaveLeaves())) {
                if (ModConfig.get().hytaleLikeFelling) {
                    return;
                }

                int chopsNeeded = tree.numChopsNeededToFell();
                int toolChops = ChopUtil.getNumChopsByTool(player.getMainHandItem(), state);

                if (chopsNeeded > toolChops && toolChops > 0) {
                    float penalty = (float) chopsNeeded / toolChops;
                    cir.setReturnValue(cir.getReturnValue() / penalty);
                }
            }
        }
    }
}