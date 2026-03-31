package com.evandev.treeliable.platform;

import com.evandev.treeliable.api.*;
import com.evandev.treeliable.common.chop.ChopResult;
import com.evandev.treeliable.common.chop.FellTreeResult;
import com.evandev.treeliable.common.platform.ModLoader;
import com.evandev.treeliable.platform.services.IPlatformHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;

import java.nio.file.Path;
import java.util.stream.Stream;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public boolean isPhysicalClient() {
        return FMLLoader.getDist() == Dist.CLIENT;
    }

    @Override
    public boolean isDedicatedServer() {
        return FMLEnvironment.dist.isDedicatedServer();
    }

    @Override
    public boolean uses(ModLoader loader) {
        return loader == ModLoader.NEOFORGE;
    }

    @Override
    public TreeData detectTreeEvent(Level level, ServerPlayer agent, BlockPos blockPos, BlockState blockState, TreeData treeData) {
        ChopEvent.DetectTreeEvent event = new ChopEvent.DetectTreeEvent(level, agent, blockPos, blockState, treeData);
        NeoForge.EVENT_BUS.post(event);

        treeData = event.getTreeData().orElse(null);
        if (event.isCanceled() || treeData == null) {
            return new AbstractTreeData() {
                @Override
                public Stream<BlockPos> streamLogs() {
                    return Stream.empty();
                }

                @Override
                public Stream<BlockPos> streamLeaves() {
                    return Stream.empty();
                }

                @Override
                public boolean hasLeaves() {
                    return false;
                }

                @Override
                public int numChopsNeededToFell() {
                    return 0;
                }
            };
        }
        return treeData;
    }

    @Override
    public boolean startChopEvent(ServerPlayer agent, ServerLevel level, BlockPos pos, BlockState blockState, ChopData chopData, Object trigger) {
        ChopEvent.StartChopEvent startChopEvent = new ChopEvent.StartChopEvent(level, agent, pos, blockState, chopData, trigger);
        NeoForge.EVENT_BUS.post(startChopEvent);
        return !startChopEvent.isCanceled();
    }

    @Override
    public void finishChopEvent(ServerPlayer agent, ServerLevel level, BlockPos pos, BlockState blockState, ChopDataImmutable chopData, ChopResult chopResult) {
        NeoForge.EVENT_BUS.post(new ChopEvent.FinishChopEvent(level, agent, pos, blockState, chopData, chopResult instanceof FellTreeResult));
    }

    @Override
    public boolean startFellTreeEvent(ServerPlayer player, Level level, BlockPos choppedPos, FellData fellData) {
        ChopEvent.BeforeFellEvent beforeFellEvent = new ChopEvent.BeforeFellEvent(level, player, choppedPos, level.getBlockState(choppedPos), fellData);
        NeoForge.EVENT_BUS.post(beforeFellEvent);
        return !beforeFellEvent.isCanceled();
    }

    @Override
    public void finishFellTreeEvent(ServerPlayer player, Level level, BlockPos choppedPos, FellData fellData) {
        ChopEvent.AfterFellEvent afterFellEvent = new ChopEvent.AfterFellEvent(level, player, choppedPos, level.getBlockState(choppedPos), fellData);
        NeoForge.EVENT_BUS.post(afterFellEvent);
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