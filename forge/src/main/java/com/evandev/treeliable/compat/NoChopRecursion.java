package com.evandev.treeliable.compat;

import com.evandev.treeliable.Treeliable;
import com.evandev.treeliable.api.ChopEvent;
import com.evandev.treeliable.common.config.ModConfig;
import com.evandev.treeliable.common.util.TickUtil;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.util.HashMap;
import java.util.Map;

public class NoChopRecursion {

    static private final Map<Player, Long> lastChopTickByPlayers = new HashMap<>();

    public static void commonSetup(FMLCommonSetupEvent event) {
        if (ModConfig.get().preventChopRecursion) {
            NeoForge.EVENT_BUS.register(EventHandler.class);
        }
    }

    private static class EventHandler {
        @SubscribeEvent
        public static void onChop(ChopEvent.StartChopEvent event) {
            Player agent = event.getPlayer();
            long time = event.getLevel().getGameTime();
            if (lastChopTickByPlayers.getOrDefault(agent, TickUtil.NEVER) == time) {
                event.setCanceled(true);
            } else {
                lastChopTickByPlayers.put(agent, time);
            }
        }
    }

}
