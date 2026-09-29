package com.evandev.treeliable;

//? if fabric {
import com.evandev.treeliable.common.FabricCommon;
import com.evandev.treeliable.common.config.ModConfig;
//? if >=1.20.5 {
import com.evandev.treeliable.common.network.ClientRequestSettingsPacket;
import com.evandev.treeliable.common.network.ServerAlgorithmSyncPacket;
import com.evandev.treeliable.common.network.ServerConfirmSettingsPacket;
import com.evandev.treeliable.common.network.ServerPermissionsPacket;
//?}
import com.evandev.treeliable.platform.server.commands.ServerCommands;
import com.evandev.treeliable.server.FabricServer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
//? if >=1.20.5
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class TreeliableFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ModConfig.load();
        Treeliable.init();

        CommonLifecycleEvents.TAGS_LOADED.register((registries, client) -> {
            Treeliable.initUsingAPI(Treeliable.api);
        });

        //? if >=26.1 {
        /*PayloadTypeRegistry.clientboundPlay().register(ServerAlgorithmSyncPacket.TYPE, ServerAlgorithmSyncPacket.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(ClientRequestSettingsPacket.TYPE, ClientRequestSettingsPacket.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ServerConfirmSettingsPacket.TYPE, ServerConfirmSettingsPacket.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ServerPermissionsPacket.TYPE, ServerPermissionsPacket.STREAM_CODEC);
        *///?} else if >=1.20.5 {
        PayloadTypeRegistry.playS2C().register(ServerAlgorithmSyncPacket.TYPE, ServerAlgorithmSyncPacket.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(ClientRequestSettingsPacket.TYPE, ClientRequestSettingsPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(ServerConfirmSettingsPacket.TYPE, ServerConfirmSettingsPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(ServerPermissionsPacket.TYPE, ServerPermissionsPacket.STREAM_CODEC);
        //?}

        PlayerBlockBreakEvents.BEFORE.register(FabricCommon::onBreakEvent);

        FabricServer.init();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ServerCommands.register(dispatcher);
        });
    }
}
//?}
