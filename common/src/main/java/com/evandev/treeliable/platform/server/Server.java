package com.evandev.treeliable.platform.server;

import com.evandev.treeliable.common.settings.ChopSettings;
import com.evandev.treeliable.common.settings.ChoppingEntity;
import com.evandev.treeliable.common.settings.SyncedChopData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public abstract class Server {

    protected static Server instance;

    public static ChopSettings getDefaultPlayerSettings() {
        return new ChopSettings();
    }

    public static Server instance() {
        return instance;
    }

    public SyncedChopData getPlayerChopData(Player player) {
        ChoppingEntity chopper = (ChoppingEntity) player;
        if (chopper.getChopData() == null) {
            chopper.setChopData(new SyncedChopData(getDefaultPlayerSettings()));
        }
        return chopper.getChopData();
    }

    public abstract void broadcast(ServerLevel level, BlockPos pos, CustomPacketPayload payload);

    public abstract void sendTo(ServerPlayer player, CustomPacketPayload payload);
}
