package com.evandev.treeliable.client;

import com.evandev.treeliable.client.integration.ClothConfigIntegration;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

public class NeoForgeClient extends Client {
    static {
        Client.instance = new NeoForgeClient();
    }

    public static void init(net.neoforged.bus.api.IEventBus modEventBus) {
        modEventBus.addListener(NeoForgeClient::onClientSetup);
        modEventBus.addListener(NeoForgeClient::onRegisterKeyMappings);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        NeoForge.EVENT_BUS.register(EventHandler.class);

        if (ModList.get().isLoaded("cloth_config")) {
            ModLoadingContext.get().registerExtensionPoint(
                    IConfigScreenFactory.class,
                    () -> (client, parent) -> ClothConfigIntegration.createScreen(parent)
            );
        }
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        KeyBindings.registerKeyMappings(event::register);
    }

    @Override
    public void sendToServer(CustomPacketPayload payload) {
        PacketDistributor.sendToServer(payload);
    }

    static class EventHandler {
        @SubscribeEvent
        public static void onConnect(ClientPlayerNetworkEvent.LoggingIn event) {
            syncOnJoin();
        }

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (event.getKey() != GLFW.GLFW_KEY_UNKNOWN) {
                for (KeyBindings.ActionableKeyBinding keyBinding : KeyBindings.allKeyBindings) {
                    if (event.getKey() == keyBinding.getKey().getValue() && event.getAction() == GLFW.GLFW_PRESS) {
                        keyBinding.onPress();
                        return;
                    }
                }
            }
        }
    }
}
