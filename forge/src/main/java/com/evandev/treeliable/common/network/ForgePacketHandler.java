package com.evandev.treeliable.common.network;

import com.evandev.treeliable.Treeliable;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ForgePacketHandler {
    private static final String PROTOCOL_VERSION = "1.0.0";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Treeliable.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int id = 0;

    public static void register() {
        CHANNEL.registerMessage(id++, ClientRequestSettingsPacket.class,
                ClientRequestSettingsPacket::encode,
                ClientRequestSettingsPacket::decode,
                (msg, ctxSupplier) -> {
                    NetworkEvent.Context ctx = ctxSupplier.get();
                    ctx.enqueueWork(() -> msg.handle(ctx.getSender(), reply -> CHANNEL.reply(reply, ctx)));
                    ctx.setPacketHandled(true);
                }
        );

        CHANNEL.registerMessage(id++, ServerConfirmSettingsPacket.class,
                ServerConfirmSettingsPacket::encode,
                ServerConfirmSettingsPacket::decode,
                (msg, ctxSupplier) -> {
                    NetworkEvent.Context ctx = ctxSupplier.get();
                    ctx.enqueueWork(msg::handle);
                    ctx.setPacketHandled(true);
                }
        );

        CHANNEL.registerMessage(id++, ServerPermissionsPacket.class,
                ServerPermissionsPacket::encode,
                ServerPermissionsPacket::decode,
                (msg, ctxSupplier) -> {
                    NetworkEvent.Context ctx = ctxSupplier.get();
                    ctx.enqueueWork(msg::handle);
                    ctx.setPacketHandled(true);
                }
        );
    }
}