package com.evandev.treeliable.client;

import com.evandev.treeliable.common.network.ClientRequestSettingsPacket;
import com.evandev.treeliable.common.network.ServerConfirmSettingsPacket;
import com.evandev.treeliable.common.network.ServerPermissionsPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;

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
        ClientPlayNetworking.registerGlobalReceiver(ServerConfirmSettingsPacket.ID, (client, handler, buf, responseSender) -> {
            ServerConfirmSettingsPacket packet = ServerConfirmSettingsPacket.decode(buf);
            client.execute(packet::handle);
        });

        ClientPlayNetworking.registerGlobalReceiver(ServerPermissionsPacket.ID, (client, handler, buf, responseSender) -> {
            ServerPermissionsPacket packet = ServerPermissionsPacket.decode(buf);
            client.execute(packet::handle);
        });
    }

    @Override
    public void sendToServer(Object payload) {
        if (payload instanceof ClientRequestSettingsPacket packet) {
            FriendlyByteBuf buf = PacketByteBufs.create();
            packet.encode(buf);
            ClientPlayNetworking.send(ClientRequestSettingsPacket.ID, buf);
        }
    }
}