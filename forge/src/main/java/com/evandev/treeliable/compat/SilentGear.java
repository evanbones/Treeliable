package com.evandev.treeliable.compat;

import com.evandev.treeliable.api.IChoppingItem;
import com.evandev.treeliable.api.TreeliableAPI;
import com.evandev.treeliable.common.config.ModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

import java.util.function.Consumer;

/**
 * Make saws perform several chops instead of 1.
 */
public class SilentGear {
    public static void enqueueIMC(InterModEnqueueEvent event) {
        if (ModConfig.get().compatForSilentGear && ModList.get().isLoaded("silentgear")) {
            InterModComms.sendTo("treeliable", "getTreeliableAPI", () -> (Consumer<TreeliableAPI>) api -> {
                Item saw = Registry.ITEM.get(new ResourceLocation("silentgear", "saw"));
                api.registerChoppingItemBehavior(saw, new IChoppingItem() {
                    @Override
                    public boolean canChop(Player player, ItemStack tool, Level level, BlockPos pos, BlockState target) {
                        return true;
                    }

                    @Override
                    public int getNumChops(ItemStack tool, BlockState target) {
                        return ModConfig.get().silentGearSawChops;
                    }
                });
            });
        }
    }
}
