package com.evandev.treeliable.mixin;

import com.evandev.treeliable.common.chop.ChopUtil;
import com.evandev.treeliable.common.util.PlacedLogTracker;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "setPlacedBy", at = @At("HEAD"))
    public void treeliable$onBlockPlaced(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack, CallbackInfo ci) {
        if (ChopUtil.isBlockChoppable(level, pos, state)) {
            PlacedLogTracker.addPlacedLog(level, pos);
        }
    }
}