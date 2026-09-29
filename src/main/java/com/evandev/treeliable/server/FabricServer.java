package com.evandev.treeliable.server;

//? if fabric {
import com.evandev.treeliable.common.chop.FellQueue;
import com.evandev.treeliable.common.config.ModConfig;
import com.evandev.treeliable.common.network.ClientRequestSettingsPacket;
import com.evandev.treeliable.common.network.PacketChannel;
import com.evandev.treeliable.common.network.ServerAlgorithmSyncPacket;
//? if <1.20.5 {
/*import com.evandev.treeliable.common.network.ServerConfirmSettingsPacket;
import com.evandev.treeliable.common.network.ServerPermissionsPacket;
*///?}
import com.evandev.treeliable.common.settings.ChoppingEntity;
import com.evandev.treeliable.common.settings.SyncedChopData;
import com.evandev.treeliable.platform.server.Server;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
//? if >=1.20.5 {
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//?} else {
/*import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;
*///?}
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

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            instance.sendTo(handler.player, new ServerAlgorithmSyncPacket(ModConfig.get()));
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> FellQueue.tick());
    }

    @Override
    public void onInitializeServer() {
        // Only run on dedicated servers, not local servers!
    }

    //? if >=1.20.5 {
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
    //?} else {
    /*private void registerPackets() {
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
        } else if (payload instanceof ServerAlgorithmSyncPacket packet) {
            packet.encode(buf);
            ServerPlayNetworking.send(player, ServerAlgorithmSyncPacket.ID, buf);
        }
    }
    *///?}
}
//?}
