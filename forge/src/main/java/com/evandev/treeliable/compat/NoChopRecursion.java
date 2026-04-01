package com.evandev.treeliable.compat;

import com.evandev.treeliable.api.ChopEvent;
import com.evandev.treeliable.common.config.ModConfig;
import com.evandev.treeliable.common.util.TickUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.HashMap;
import java.util.Map;

public class NoChopRecursion {

    static private final Map<Player, Long> lastChopTickByPlayers = new HashMap<>();

    public static void commonSetup(FMLCommonSetupEvent event) {
        if (ModConfig.get().preventChopRecursion) {
            MinecraftForge.EVENT_BUS.register(EventHandler.class);
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
