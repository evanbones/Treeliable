package com.evandev.treeliable.common.network;

//? if >=1.20.5
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

@FunctionalInterface
public interface PacketChannel {
    //? if >=1.20.5 {
    void send(CustomPacketPayload packet);
    //?} else
    //void send(Object packet);
}
