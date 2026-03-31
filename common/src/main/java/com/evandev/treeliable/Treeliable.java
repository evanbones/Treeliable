package com.evandev.treeliable;

import com.evandev.treeliable.api.TreeliableAPI;
import com.evandev.treeliable.compat.HugeFungusHandler;
import com.evandev.treeliable.compat.HugeMushroomHandler;
import com.evandev.treeliable.compat.LeafDecayOverrides;
import com.evandev.treeliable.compat.ProblematicLeavesTreeHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Treeliable {
    public static final String MOD_ID = "treeliable";
    public static final String MOD_NAME = "Treeliable";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static TreeliableInternalAPI api;
    private static int cryCounter = 10;

    public static void init() {
        api = new TreeliableInternalAPI(MOD_ID);
        initUsingAPI(api);
    }

    public static void initUsingAPI(TreeliableAPI api) {
        HugeMushroomHandler.register(api);
        HugeFungusHandler.register(api);
        ProblematicLeavesTreeHandler.register(api);
        LeafDecayOverrides.register(api);
    }

    @SuppressWarnings("ConstantConditions")
    public static void showText(String text) {
        Minecraft.getInstance().player.displayClientMessage(Component.literal(String.format("%s[%s] %s%s", ChatFormatting.GRAY, Treeliable.MOD_NAME, ChatFormatting.WHITE, text)), false);
    }

    public static ResourceLocation resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(Treeliable.MOD_ID, path);
    }

    public static void cry(Throwable e) {
        if (cryCounter-- > 0) {
            LOGGER.error("Something went wrong - please share this log file at https://github.com/evanbones/treeliable/issues", e);
        }
    }
}
