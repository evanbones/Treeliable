package com.evandev.treeliable;

import com.evandev.treeliable.client.NeoForgeClient;
import com.evandev.treeliable.common.config.ModConfig;
import com.evandev.treeliable.common.network.NeoForgePacketHandler;
import com.evandev.treeliable.compat.Apotheosis;
import com.evandev.treeliable.compat.NoChopOnRightClick;
import com.evandev.treeliable.compat.NoChopRecursion;
import com.evandev.treeliable.compat.SilentGear;
import com.evandev.treeliable.platform.server.commands.ServerCommands;
import com.evandev.treeliable.server.NeoForgeServer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod(Treeliable.MOD_ID)
public class TreeliableNeoForge {
    public TreeliableNeoForge(IEventBus modEventBus) {
        ModConfig.load();
        Treeliable.init();
        modEventBus.addListener(NeoForgePacketHandler::registerPayloads);

        modEventBus.addListener(Apotheosis::commonSetup);
        modEventBus.addListener(NoChopOnRightClick::commonSetup);
        modEventBus.addListener(NoChopRecursion::commonSetup);
        modEventBus.addListener(SilentGear::enqueueIMC);

        NeoForge.EVENT_BUS.addListener(this::onRegisterCommands);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            NeoForgeClient.init(modEventBus);
        }

        NeoForgeServer.init(modEventBus);
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        ServerCommands.register(event.getDispatcher());
    }
}