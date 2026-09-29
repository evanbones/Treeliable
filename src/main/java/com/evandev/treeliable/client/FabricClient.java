package com.evandev.treeliable.client;

//? if fabric {
//? if <1.20.5
//import com.evandev.treeliable.common.network.ClientRequestSettingsPacket;
import com.evandev.treeliable.common.network.ServerAlgorithmSyncPacket;
import com.evandev.treeliable.common.network.ServerConfirmSettingsPacket;
import com.evandev.treeliable.common.network.ServerPermissionsPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
//? if >=26.1 {
//import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
//?} else
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
//? if >=1.20.5 {
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//?} else {
/*import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;
*///?}

@Environment(EnvType.CLIENT)
public class FabricClient extends Client implements ClientModInitializer {
    static {
        Client.instance = new FabricClient();
    }

    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> syncOnJoin());

        registerPackets();
        registerKeybindings();
    }

    private void registerKeybindings() {
        //? if >=26.1 {
        //KeyBindings.registerKeyMappings(KeyMappingHelper::registerKeyMapping);
        //?} else
        KeyBindings.registerKeyMappings(KeyBindingHelper::registerKeyBinding);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for (KeyBindings.ActionableKeyBinding keyBinding : KeyBindings.allKeyBindings) {
                if (keyBinding.consumeClick()) {
                    keyBinding.onPress();
                    return;
                }
            }
        });
    }

    private void registerPackets() {
        //? if >=1.20.5 {
        ClientPlayNetworking.registerGlobalReceiver(ServerConfirmSettingsPacket.TYPE, (payload, context) -> payload.handle());
        ClientPlayNetworking.registerGlobalReceiver(ServerPermissionsPacket.TYPE, (payload, context) -> payload.handle());
        ClientPlayNetworking.registerGlobalReceiver(ServerAlgorithmSyncPacket.TYPE, (payload, context) -> payload.handle());
        //?} else {
        /*ClientPlayNetworking.registerGlobalReceiver(ServerConfirmSettingsPacket.ID, (client, handler, buf, responseSender) -> {
            ServerConfirmSettingsPacket packet = ServerConfirmSettingsPacket.decode(buf);
            client.execute(packet::handle);
        });

        ClientPlayNetworking.registerGlobalReceiver(ServerPermissionsPacket.ID, (client, handler, buf, responseSender) -> {
            ServerPermissionsPacket packet = ServerPermissionsPacket.decode(buf);
            client.execute(packet::handle);
        });

        ClientPlayNetworking.registerGlobalReceiver(ServerAlgorithmSyncPacket.ID, (client, handler, buf, responseSender) -> {
            ServerAlgorithmSyncPacket packet = ServerAlgorithmSyncPacket.decode(buf);
            client.execute(packet::handle);
        });
        *///?}
    }

    //? if >=1.20.5 {
    @Override
    public void sendToServer(CustomPacketPayload payload) {
        ClientPlayNetworking.send(payload);
    }
    //?} else {
    /*@Override
    public void sendToServer(Object payload) {
        if (payload instanceof ClientRequestSettingsPacket packet) {
            FriendlyByteBuf buf = PacketByteBufs.create();
            packet.encode(buf);
            ClientPlayNetworking.send(ClientRequestSettingsPacket.ID, buf);
        }
    }
    *///?}
}
//?}
