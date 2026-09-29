package com.evandev.treeliable.mixin;

import com.evandev.treeliable.common.chop.ChopUtil;
import com.evandev.treeliable.common.util.PlacedLogTracker;
import net.minecraft.core.BlockPos;
//? if >=26.1 {
//import net.minecraft.server.level.ServerLevel;
//?} else
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {
    //? if >=26.1 {
    /*@Inject(method = "affectNeighborsAfterRemoval", at = @At("HEAD"))
    public void treeliable$onBlockRemoved(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston, CallbackInfo ci) {
        BlockState newState = level.getBlockState(pos);

    *///?} else {
    @Inject(method = "onRemove", at = @At("HEAD"))
    public void treeliable$onBlockRemoved(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving, CallbackInfo ci) {
    //?}
        if (!state.is(newState.getBlock())) {
            if (!ChopUtil.isBlockChoppable(level, pos, newState)) {
                PlacedLogTracker.removePlacedLog(level, pos);
            }
        }
    }
}
