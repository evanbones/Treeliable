package com.evandev.treeliable.common.network;

import com.evandev.treeliable.Treeliable;
import com.evandev.treeliable.common.config.ChopCountingAlgorithm;
import com.evandev.treeliable.common.config.ModConfig;
import com.evandev.treeliable.common.config.Rounder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class ServerAlgorithmSyncPacket implements CustomPacketPayload {
    public static final Identifier ID = Treeliable.resource("server_algorithm_sync");
    public static final CustomPacketPayload.Type<ServerAlgorithmSyncPacket> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<FriendlyByteBuf, ServerAlgorithmSyncPacket> STREAM_CODEC = CustomPacketPayload.codec(
            ServerAlgorithmSyncPacket::encode, ServerAlgorithmSyncPacket::decode
    );

    private final ChopCountingAlgorithm algorithm;
    private final Rounder rounder;
    private final boolean canRequireMore;
    private final double logarithmicA;
    private final double linearM;
    private final double linearB;

    public ServerAlgorithmSyncPacket(ModConfig config) {
        this.algorithm = config.chopCountingAlgorithm;
        this.rounder = config.chopCountRounding;
        this.canRequireMore = config.canRequireMoreChopsThanBlocks;
        this.logarithmicA = config.logarithmicA;
        this.linearM = config.linearM;
        this.linearB = config.linearB;
    }

    private ServerAlgorithmSyncPacket(ChopCountingAlgorithm algorithm, Rounder rounder, boolean canRequireMore, double logarithmicA, double linearM, double linearB) {
        this.algorithm = algorithm;
        this.rounder = rounder;
        this.canRequireMore = canRequireMore;
        this.logarithmicA = logarithmicA;
        this.linearM = linearM;
        this.linearB = linearB;
    }

    public static ServerAlgorithmSyncPacket decode(FriendlyByteBuf buffer) {
        return new ServerAlgorithmSyncPacket(
                buffer.readEnum(ChopCountingAlgorithm.class),
                buffer.readEnum(Rounder.class),
                buffer.readBoolean(),
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readDouble()
        );
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeEnum(algorithm);
        buffer.writeEnum(rounder);
        buffer.writeBoolean(canRequireMore);
        buffer.writeDouble(logarithmicA);
        buffer.writeDouble(linearM);
        buffer.writeDouble(linearB);
    }

    public void handle() {
        ModConfig config = ModConfig.get();
        config.chopCountingAlgorithm = this.algorithm;
        config.chopCountRounding = this.rounder;
        config.canRequireMoreChopsThanBlocks = this.canRequireMore;
        config.logarithmicA = this.logarithmicA;
        config.linearM = this.linearM;
        config.linearB = this.linearB;
        config.invalidateCaches();
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}