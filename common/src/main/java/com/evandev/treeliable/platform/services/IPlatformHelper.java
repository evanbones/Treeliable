package com.evandev.treeliable.platform.services;

import com.evandev.treeliable.api.ChopData;
import com.evandev.treeliable.api.ChopDataImmutable;
import com.evandev.treeliable.api.FellData;
import com.evandev.treeliable.api.TreeData;
import com.evandev.treeliable.common.chop.ChopResult;
import com.evandev.treeliable.common.platform.ModLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.nio.file.Path;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }

    /**
     * Gets the configuration directory for the current platform.
     *
     * @return The path to the config directory.
     */
    Path getConfigDirectory();

    /**
     * Checks if the code is running on the physical client.
     *
     * @return True if on the client, false if on a dedicated server.
     */
    boolean isPhysicalClient();

    boolean isDedicatedServer();

    boolean uses(ModLoader loader);

    TreeData detectTreeEvent(Level level, ServerPlayer player, BlockPos blockPos, BlockState blockState, TreeData treeData);

    boolean startChopEvent(ServerPlayer player, ServerLevel level, BlockPos pos, BlockState blockState, ChopData chopData, Object trigger);

    void finishChopEvent(ServerPlayer player, ServerLevel level, BlockPos pos, BlockState blockState, ChopDataImmutable chopData, ChopResult chopResult);

    Identifier getIdentifierForBlock(Block block);

    Identifier getIdentifierForItem(Item item);

    boolean startFellTreeEvent(ServerPlayer player, Level level, BlockPos choppedPos, FellData fellData);

    void finishFellTreeEvent(ServerPlayer player, Level level, BlockPos choppedPos, FellData fellData);
}