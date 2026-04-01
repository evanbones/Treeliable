package com.evandev.treeliable;

import com.evandev.treeliable.common.FabricCommon;
import com.evandev.treeliable.common.config.ModConfig;
import com.evandev.treeliable.platform.server.commands.ServerCommands;
import com.evandev.treeliable.server.FabricServer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;

public class TreeliableFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ModConfig.load();
        Treeliable.init();

        PlayerBlockBreakEvents.BEFORE.register(FabricCommon::onBreakEvent);

        FabricServer.init();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ServerCommands.register(dispatcher);
        });
    }
}