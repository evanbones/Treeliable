package com.evandev.treeliable.compat;

//? if forgelike {
/*import com.evandev.treeliable.api.ChopEvent;
import com.evandev.treeliable.common.config.ModConfig;
//? if >=1.21 {
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
//?}
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
//? if neoforge {
/^import net.neoforged.bus.api.EventPriority;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;
^///?} else {
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
//?}

public class Apotheosis {

    public static void commonSetup(FMLCommonSetupEvent event) {
        if (ModConfig.get().compatForApotheosis && ModList.get().isLoaded("apothic_enchanting")) {
            //? if neoforge {
            //NeoForge.EVENT_BUS.addListener(EventPriority.NORMAL, Apotheosis::onChop);
            //?} else
            MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, Apotheosis::onChop);
        }
    }

    public static void onChop(ChopEvent.StartChopEvent event) {
        //? if >=1.21 {
        final ResourceKey<Enchantment> chainsaw_key = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath("apothic_enchanting", "chainsaw"));
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
        //?} else {
        /^ItemStack tool = event.getPlayer().getMainHandItem();
        Enchantment chainsaw = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation("apothic_enchanting", "chainsaw"));

        if (chainsaw != null && tool.getEnchantmentLevel(chainsaw) > 0) {
            if (event.getPlayer() instanceof FakePlayer) {
                event.setCanceled(true);
            } else {
                event.setNumChops(100);
            }
        }
        ^///?}
    }
}
*///?}
