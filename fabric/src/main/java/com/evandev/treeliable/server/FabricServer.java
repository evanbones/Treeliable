package com.evandev.treeliable.server;

import com.evandev.treeliable.common.chop.FellQueue;
import com.evandev.treeliable.common.network.ClientRequestSettingsPacket;
import com.evandev.treeliable.common.network.PacketChannel;
import com.evandev.treeliable.common.network.ServerConfirmSettingsPacket;
import com.evandev.treeliable.common.network.ServerPermissionsPacket;
import com.evandev.treeliable.common.settings.ChoppingEntity;
import com.evandev.treeliable.common.settings.SyncedChopData;
import com.evandev.treeliable.platform.server.Server;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
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

        ServerTickEvents.END_SERVER_TICK.register(server -> FellQueue.tick());
    }

    @Override
    public void onInitializeServer() {
        // Only run on dedicated servers, not local servers!
    }

    private void registerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(ClientRequestSettingsPacket.ID, (server, player, handler, buf, responseSender) -> {
            ClientRequestSettingsPacket packet = ClientRequestSettingsPacket.decode(buf);
            server.execute(() -> packet.handle(player, responseChannel(player)));
        });
    }

    private PacketChannel responseChannel(ServerPlayer player) {
        return reply -> sendTo(player, reply);
    }

    @Override
    public void broadcast(ServerLevel level, BlockPos pos, Object payload) {
        for (ServerPlayer player : PlayerLookup.tracking(level, pos)) {
            sendTo(player, payload);
        }
    }

    @Override
    public void sendTo(ServerPlayer player, Object payload) {
        FriendlyByteBuf buf = PacketByteBufs.create();

        if (payload instanceof ServerConfirmSettingsPacket packet) {
            packet.encode(buf);
            ServerPlayNetworking.send(player, ServerConfirmSettingsPacket.ID, buf);
        } else if (payload instanceof ServerPermissionsPacket packet) {
            packet.encode(buf);
            ServerPlayNetworking.send(player, ServerPermissionsPacket.ID, buf);
        }
    }
}