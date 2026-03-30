package com.evandev.treeliable;

import com.evandev.treeliable.common.config.ModConfig;
import com.evandev.treeliable.server.FabricServer;
import net.fabricmc.api.ModInitializer;

public class TreeliableFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ModConfig.load();
        Treeliable.init();
        FabricServer.init();
    }
}