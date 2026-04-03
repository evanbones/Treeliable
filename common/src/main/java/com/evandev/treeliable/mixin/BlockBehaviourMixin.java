package com.evandev.treeliable.mixin;

import com.evandev.treeliable.common.util.PlacedLogTracker;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {

    @Inject(method = "affectNeighborsAfterRemoval", at = @At("HEAD"))
    public void treeliable$onBlockRemoved(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston, CallbackInfo ci) {
        BlockState newState = level.getBlockState(pos);

        if (!state.is(newState.getBlock())) {
            PlacedLogTracker.removePlacedLog(level, pos);
        }
    }
}