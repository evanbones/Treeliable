package com.evandev.treeliable.common.network;

@FunctionalInterface
public interface PacketChannel {
    void send(Object packet);
}