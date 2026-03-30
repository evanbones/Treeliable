package com.evandev.treeliable.server;

import com.evandev.treeliable.common.network.ClientRequestSettingsPacket;
import com.evandev.treeliable.common.network.PacketChannel;
import com.evandev.treeliable.common.settings.ChoppingEntity;
import com.evandev.treeliable.common.settings.SyncedChopData;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class FabricServer extends Server implements DedicatedServerModInitializer {

    public static void init() {
        FabricServer instance = new FabricServer();
        instance.registerPackets();
        Server.instance = instance;

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            SyncedChopData chopSettings = instance.getPlayerChopData(oldPlayer);
            ((ChoppingEntity) newPlayer).setChopData(chopSettings);
        });
    }

    @Override
    public void onInitializeServer() {
        // Only run on dedicated servers, not local servers!
    }

    private void registerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(ClientRequestSettingsPacket.TYPE, (payload, context) ->
                payload.handle(context.player(), responseChannel(context)));
    }

    private PacketChannel responseChannel(ServerPlayNetworking.Context context) {
        return reply -> ServerPlayNetworking.send(context.player(), reply);
    }

    @Override
    public void broadcast(ServerLevel level, BlockPos pos, CustomPacketPayload payload) {
        for (ServerPlayer player : PlayerLookup.tracking(level, pos)) {
            instance().sendTo(player, payload);
        }
    }

    @Override
    public void sendTo(ServerPlayer player, CustomPacketPayload payload) {
        ServerPlayNetworking.send(player, payload);
    }
}
