package com.evandev.treeliable.compat;

import com.evandev.treeliable.api.ChopEvent;
import com.evandev.treeliable.common.config.ModConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class Apotheosis {

    public static void commonSetup(FMLCommonSetupEvent event) {
        if (ModConfig.get().compatForApotheosis && ModList.get().isLoaded("apothic_enchanting")) {
            MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, Apotheosis::onChop);
        }
    }

    public static void onChop(ChopEvent.StartChopEvent event) {
        ItemStack tool = event.getPlayer().getMainHandItem();
        Enchantment chainsaw = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation("apothic_enchanting", "chainsaw"));

        if (chainsaw != null && tool.getEnchantmentLevel(chainsaw) > 0) {
            if (event.getPlayer() instanceof FakePlayer) {
                event.setCanceled(true);
            } else {
                event.setNumChops(100);
            }
        }
    }
}