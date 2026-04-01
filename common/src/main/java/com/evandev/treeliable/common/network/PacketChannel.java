package com.evandev.treeliable.common.network;

@FunctionalInterface
public interface PacketChannel {
    void send(CustomPacketPayload packet);
}
