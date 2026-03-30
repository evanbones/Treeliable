package com.evandev.treeliable.platform;

import com.evandev.treeliable.api.*;
import com.evandev.treeliable.common.chop.ChopResult;
import com.evandev.treeliable.common.chop.FellTreeResult;
import com.evandev.treeliable.common.platform.ModLoader;
import com.evandev.treeliable.platform.services.IPlatformHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.nio.file.Path;

public class FabricPlatformHelper implements IPlatformHelper {
    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public boolean isPhysicalClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    @Override
    public boolean isDedicatedServer() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER;
    }

    @Override
    public boolean uses(ModLoader loader) {
        return loader == ModLoader.FABRIC;
    }

    @Override
    public TreeData detectTreeEvent(Level level, ServerPlayer player, BlockPos blockPos, BlockState blockState, TreeData treeData) {
        return TreeChopEvents.DETECT_TREE.invoker().onDetectTree(level, player, blockPos, blockState, treeData);
    }

    @Override
    public boolean startChopEvent(ServerPlayer agent, ServerLevel level, BlockPos pos, BlockState blockState, ChopData chopData, Object trigger) {
        return TreeChopEvents.BEFORE_CHOP.invoker().beforeChop(level, agent, pos, blockState, chopData);
    }

    @Override
    public void finishChopEvent(ServerPlayer agent, ServerLevel level, BlockPos pos, BlockState blockState, ChopDataImmutable chopData, ChopResult chopResult) {
        TreeChopEvents.AFTER_CHOP.invoker().afterChop(level, agent, pos, blockState, chopData, chopResult instanceof FellTreeResult);
    }

    @Override
    public boolean startFellTreeEvent(ServerPlayer player, Level level, BlockPos choppedPos, FellData fellData) {
        return TreeChopEvents.BEFORE_FELL.invoker().beforeFell(level, player, choppedPos, fellData);
    }

    @Override
    public void finishFellTreeEvent(ServerPlayer player, Level level, BlockPos choppedPos, FellData fellData) {
        TreeChopEvents.AFTER_FELL.invoker().afterFell(level, player, choppedPos, fellData);
    }

    @Override
    public ResourceLocation getResourceLocationForBlock(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    @Override
    public ResourceLocation getResourceLocationForItem(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }
}