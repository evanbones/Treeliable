package com.evandev.treeliable.client;

import com.evandev.treeliable.Treeliable;
import com.evandev.treeliable.client.settings.ClientChopSettings;
import com.evandev.treeliable.common.config.ModConfig;
import com.evandev.treeliable.common.network.ClientRequestSettingsPacket;
import com.evandev.treeliable.common.settings.ChopSettings;
import com.evandev.treeliable.common.settings.Permissions;
import com.evandev.treeliable.common.settings.SettingsField;
import com.evandev.treeliable.common.settings.SneakBehavior;
import com.evandev.treeliable.common.util.TreeCache;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public abstract class Client {
    protected static final Permissions serverPermissions = new Permissions();
    public static TreeCache treeCache = new TreeCache();
    protected static Client instance;
    protected static final ClientChopSettings chopSettings = new ClientChopSettings() {
        @Override
        public ChopSettings set(SettingsField field, Object value) {
            treeCache.invalidate();
            return super.set(field, value);
        }
    };

    public static void requestSetting(SettingsField field, Object value) {
        Client.instance().sendToServer(new ClientRequestSettingsPacket(field, value));
    }

    public static void toggleChopping() {
        boolean newValue = !chopSettings.get(SettingsField.CHOPPING, Boolean.class);
        chopSettings.set(SettingsField.CHOPPING, newValue);
    }

    public static void cycleSneakBehavior() {
        SneakBehavior newValue = chopSettings.getSneakBehavior() == SneakBehavior.NONE ? SneakBehavior.INVERT_CHOPPING : SneakBehavior.NONE;
        chopSettings.set(SettingsField.SNEAK_BEHAVIOR, newValue);
    }

    public static ClientChopSettings getChopSettings() {
        return chopSettings;
    }

    public static void setChoppingIndicatorVisibility(boolean showChoppingIndicator) {
        ModConfig.get().showChoppingIndicators = showChoppingIndicator;
        ModConfig.save();
    }

    public static boolean isChoppingIndicatorEnabled() {
        return ModConfig.get().showChoppingIndicators;
    }

    public static void updatePermissions(Permissions permissions) {
        serverPermissions.copy(permissions);
    }

    public static Permissions getServerPermissions() {
        return serverPermissions;
    }

    public static Client instance() {
        return instance;
    }

    protected static void syncOnJoin() {
        Treeliable.LOGGER.info("Sending chop settings sync request");

        ModConfig config = ModConfig.get();
        chopSettings.setChoppingEnabled(config.choppingEnabled);
        chopSettings.setSneakBehavior(config.sneakBehavior);
        chopSettings.setTreesMustHaveLeaves(config.treesMustHaveLeaves);
        chopSettings.setChopInCreativeMode(config.chopInCreativeMode);

        Client.instance().sendToServer(new ClientRequestSettingsPacket(chopSettings));
    }

    public static Player getPlayer() {
        return Minecraft.getInstance().player;
    }

    abstract public void sendToServer(CustomPacketPayload payload);
}
