package com.evandev.treeliable.client;

//? if neoforge {
/*import com.evandev.treeliable.client.integration.YaclConfigIntegration;
import com.mojang.blaze3d.platform.InputConstants;
import com.evandev.treeliable.platform.Services;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
//? if >=26.1 {
//import net.neoforged.neoforge.client.network.ClientPacketDistributor;
//?} else
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.common.NeoForge;

public class NeoForgeClient extends Client {
    static {
        Client.instance = new NeoForgeClient();
    }

    public static void init(IEventBus modEventBus) {
        modEventBus.addListener(NeoForgeClient::onClientSetup);
        modEventBus.addListener(NeoForgeClient::onRegisterKeyMappings);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        NeoForge.EVENT_BUS.register(EventHandler.class);

        if (Services.PLATFORM.isModLoaded("yet_another_config_lib_v3")) {
            ModLoadingContext.get().registerExtensionPoint(
                    IConfigScreenFactory.class,
                    () -> (client, parent) -> YaclConfigIntegration.createScreen(parent)
            );
        }
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        KeyBindings.registerKeyMappings(event::register);
    }

    @Override
    public void sendToServer(CustomPacketPayload payload) {
        //? if >=26.1 {
        //ClientPacketDistributor.sendToServer(payload);
        //?} else
        PacketDistributor.sendToServer(payload);
    }

    static class EventHandler {
        @SubscribeEvent
        public static void onConnect(ClientPlayerNetworkEvent.LoggingIn event) {
            syncOnJoin();
        }

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (event.getKey() != InputConstants.UNKNOWN.getValue()) {
                for (KeyBindings.ActionableKeyBinding keyBinding : KeyBindings.allKeyBindings) {
                    if (event.getKey() == keyBinding.getKey().getValue() && event.getAction() == InputConstants.PRESS) {
                        keyBinding.onPress();
                        return;
                    }
                }
            }
        }
    }
}
*///?}
