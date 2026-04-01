package com.evandev.treeliable;

import com.evandev.treeliable.client.ForgeClient;
import com.evandev.treeliable.common.config.ModConfig;
import com.evandev.treeliable.common.network.ForgePacketHandler;
import com.evandev.treeliable.compat.Apotheosis;
import com.evandev.treeliable.compat.NoChopOnRightClick;
import com.evandev.treeliable.compat.NoChopRecursion;
import com.evandev.treeliable.compat.SilentGear;
import com.evandev.treeliable.platform.server.commands.ServerCommands;
import com.evandev.treeliable.server.ForgeServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(Treeliable.MOD_ID)
public class TreeliableForge {
    public TreeliableForge(IEventBus modEventBus) {
        ModConfig.load();
        Treeliable.init();
        modEventBus.addListener(ForgePacketHandler::registerPayloads);

        modEventBus.addListener(Apotheosis::commonSetup);
        modEventBus.addListener(NoChopOnRightClick::commonSetup);
        modEventBus.addListener(NoChopRecursion::commonSetup);
        modEventBus.addListener(SilentGear::enqueueIMC);

        MinecraftForge.EVENT_BUS.addListener(this::onRegisterCommands);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            ForgeClient.init(modEventBus);
        }

        ForgeServer.init(modEventBus);
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        ServerCommands.register(event.getDispatcher());
    }
}