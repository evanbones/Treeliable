package com.evandev.treeliable.common.network;

import com.evandev.treeliable.Treeliable;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NeoForgePacketHandler {

    public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(Treeliable.MOD_ID).versioned("1.0.0");

        registrar.playToServer(
                ClientRequestSettingsPacket.TYPE,
                ClientRequestSettingsPacket.STREAM_CODEC,
                (payload, context) -> payload.handle(context.player(), context::reply)
        );

        registrar.playToClient(
                ServerConfirmSettingsPacket.TYPE,
                ServerConfirmSettingsPacket.STREAM_CODEC,
                (payload, context) -> payload.handle()
        );

        registrar.playToClient(
                ServerPermissionsPacket.TYPE,
                ServerPermissionsPacket.STREAM_CODEC,
                (payload, context) -> payload.handle()
        );
    }
}