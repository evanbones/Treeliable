package com.evandev.treeliable.compat;

import com.evandev.treeliable.api.ChopEvent;
import com.evandev.treeliable.common.config.ModConfig;
import com.evandev.treeliable.common.util.TickUtil;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.HashMap;
import java.util.Map;

public class NoChopOnRightClick {

    private static final Map<Entity, Long> lastRightClickTickByPlayers = new HashMap<>();
    private static boolean enabled = false;

    public static void commonSetup(FMLCommonSetupEvent event) {
        if (ModConfig.get().preventChoppingOnRightClick) {
            enable();
        }
    }

    public static void enable() {
        if (!enabled) {
            MinecraftForge.EVENT_BUS.register(EventHandler.class);
            enabled = true;
        }
    }

    private static class EventHandler {
        @SubscribeEvent
        public static void onBlockStartClick(PlayerInteractEvent.RightClickBlock event) {
            long time = event.getLevel().getGameTime();
            lastRightClickTickByPlayers.put(event.getEntity(), time);
        }

        @SubscribeEvent
        public static void onChop(ChopEvent.StartChopEvent event) {
            long time = event.getLevel().getGameTime();
            if (lastRightClickTickByPlayers.getOrDefault(event.getPlayer(), TickUtil.NEVER) == time) {
                event.setCanceled(true);
            }
        }
    }
}