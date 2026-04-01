package com.evandev.treeliable.server;

import com.evandev.treeliable.common.chop.FellQueue;
import com.evandev.treeliable.common.settings.ChoppingEntity;
import com.evandev.treeliable.common.settings.SyncedChopData;
import com.evandev.treeliable.platform.server.Server;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.PacketDistributor;

public class ForgeServer extends com.evandev.treeliable.platform.server.Server {
    static {
        Server.instance = new ForgeServer();
    }

    public static void init(IEventBus modEventBus) {
        modEventBus.addListener(ForgeServer::onCommonSetup);
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(EventHandler.class);
    }

    @Override
    public void broadcast(ServerLevel level, BlockPos pos, CustomPacketPayload payload) {
        PacketDistributor.sendToPlayersTrackingChunk(level, new ChunkPos(pos), payload);
    }

    @Override
    public void sendTo(ServerPlayer player, CustomPacketPayload payload) {
        PacketDistributor.sendToPlayer(player, payload);
    }

    private static class EventHandler {
        @SubscribeEvent
        public static void onPlayerCloned(PlayerEvent.Clone event) {
            if (event.isWasDeath()) {
                Player oldPlayer = event.getOriginal();
                Player newPlayer = event.getEntity();

                SyncedChopData chopSettings = instance.getPlayerChopData(oldPlayer);
                ((ChoppingEntity) newPlayer).setChopData(chopSettings);
            }
        }

        @SubscribeEvent
        public static void onServerTick(TickEvent.ServerTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                FellQueue.tick();
            }
        }
    }
}
