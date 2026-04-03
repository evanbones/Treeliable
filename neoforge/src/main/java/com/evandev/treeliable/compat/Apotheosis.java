package com.evandev.treeliable.compat;

import com.evandev.treeliable.api.ChopEvent;
import com.evandev.treeliable.common.config.ModConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;

public class Apotheosis {

    public static void commonSetup(FMLCommonSetupEvent event) {
        if (ModConfig.get().compatForApotheosis && ModList.get().isLoaded("apothic_enchanting")) {
            NeoForge.EVENT_BUS.addListener(EventPriority.NORMAL, Apotheosis::onChop);
        }
    }

    public static void onChop(ChopEvent.StartChopEvent event) {
        final ResourceKey<Enchantment> chainsaw_key = ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath("apothic_enchanting", "chainsaw"));
        ItemStack tool = event.getPlayer().getMainHandItem();

        event.getLevel().registryAccess().lookup(Registries.ENCHANTMENT)
                .flatMap(reg -> reg.get(chainsaw_key))
                .ifPresent(chainsaw -> {
                    if (tool.getEnchantmentLevel(chainsaw) > 0) {
                        if (event.getPlayer() instanceof FakePlayer) {
                            event.setCanceled(true);
                        } else {
                            event.setNumChops(100);
                        }
                    }
                });
    }
}