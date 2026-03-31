package com.evandev.treeliable;

import com.evandev.treeliable.common.FabricCommon;
import com.evandev.treeliable.common.config.ModConfig;
import com.evandev.treeliable.common.network.ClientRequestSettingsPacket;
import com.evandev.treeliable.common.network.ServerConfirmSettingsPacket;
import com.evandev.treeliable.common.network.ServerPermissionsPacket;
import com.evandev.treeliable.platform.server.commands.ServerCommands;
import com.evandev.treeliable.server.FabricServer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class TreeliableFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ModConfig.load();
        Treeliable.init();

        PayloadTypeRegistry.playC2S().register(ClientRequestSettingsPacket.TYPE, ClientRequestSettingsPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(ServerConfirmSettingsPacket.TYPE, ServerConfirmSettingsPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(ServerPermissionsPacket.TYPE, ServerPermissionsPacket.STREAM_CODEC);

        PlayerBlockBreakEvents.BEFORE.register(FabricCommon::onBreakEvent);

        FabricServer.init();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ServerCommands.register(dispatcher);
        });
    }
}