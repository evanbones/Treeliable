package com.evandev.treeliable.common.network;

import com.evandev.treeliable.Treeliable;
import com.evandev.treeliable.client.settings.ClientChopSettings;
import com.evandev.treeliable.common.config.ModConfig;
import com.evandev.treeliable.common.settings.ChopSettings;
import com.evandev.treeliable.common.settings.Setting;
import com.evandev.treeliable.common.settings.SettingsField;
import com.evandev.treeliable.common.settings.SyncedChopData;
import com.evandev.treeliable.platform.server.Server;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ClientRequestSettingsPacket {
    public static final ResourceLocation ID = Treeliable.resource("client_request_settings");

    private final List<Setting> settings;
    private final Event event;

    public ClientRequestSettingsPacket(final List<Setting> settings, Event event) {
        this.settings = settings;
        this.event = event;
    }

    public ClientRequestSettingsPacket(SettingsField field, Object value) {
        this(Collections.singletonList(new Setting(field, value)), Event.REQUEST);
    }

    public ClientRequestSettingsPacket(ClientChopSettings chopSettings) {
        this(chopSettings.getAll(), Event.FIRST_TIME_SYNC);
    }

    public static ClientRequestSettingsPacket decode(FriendlyByteBuf buffer) {
        Event event = Event.decode(buffer);
        int numSettings = buffer.readInt();
        List<Setting> settings = IntStream.range(0, numSettings)
                .mapToObj($ -> Setting.decode(buffer))
                .collect(Collectors.toList());

        return new ClientRequestSettingsPacket(settings, event);
    }

    private static void processSettingsRequest(SyncedChopData chopData, ClientRequestSettingsPacket message, Player player, PacketChannel replyChannel) {
        List<Setting> settings = (message.event == Event.FIRST_TIME_SYNC && chopData.isSynced())
                ? chopData.getSettings().getAll()
                : message.settings;

        List<ConfirmedSetting> confirmedSettings = settings.stream()
                .map(setting -> processSingleSettingRequest(setting, player, chopData.getSettings(), message.event))
                .collect(Collectors.toList());

        if (!chopData.isSynced()) {
            chopData.setSynced();
        }

        replyChannel.send(new ServerConfirmSettingsPacket(confirmedSettings));

        if (message.event == Event.FIRST_TIME_SYNC) {
            replyChannel.send(new ServerPermissionsPacket(ModConfig.getServerPermissions()));
        }
    }

    private static ConfirmedSetting processSingleSettingRequest(Setting setting, Player player, ChopSettings chopSettings, Event requestEvent) {
        ConfirmedSetting.Event confirmEvent;
        if (playerHasPermission(player, setting)) {
            chopSettings.set(setting);
            confirmEvent = ConfirmedSetting.Event.ACCEPT;
        } else {
            Setting defaultSetting = getDefaultSetting(player, setting);
            chopSettings.set(defaultSetting);
            confirmEvent = ConfirmedSetting.Event.DENY;
        }

        if (requestEvent == Event.FIRST_TIME_SYNC) {
            confirmEvent = ConfirmedSetting.Event.SILENT;
        }

        SettingsField field = setting.getField();
        return new ConfirmedSetting(new Setting(field, chopSettings.get(field)), confirmEvent);
    }

    private static Setting getDefaultSetting(Player player, Setting setting) {
        return Server.getDefaultPlayerSettings().getSetting(setting.getField());
    }

    private static boolean playerHasPermission(Player player, Setting setting) {
        return ModConfig.getServerPermissions().isPermitted(setting);
    }

    public void encode(FriendlyByteBuf buffer) {
        event.encode(buffer);
        buffer.writeInt(settings.size());
        settings.forEach(setting -> setting.encode(buffer));
    }

    public void handle(Player player, PacketChannel replyChannel) {
        processSettingsRequest(Server.instance().getPlayerChopData(player), this, player, replyChannel);
    }

    public enum Event {
        FIRST_TIME_SYNC,
        REQUEST;

        private static final Event[] values = Event.values();

        public static Event decode(FriendlyByteBuf buffer) {
            int ordinal = buffer.readByte() % values.length;
            return Event.values[ordinal];
        }

        public void encode(FriendlyByteBuf buffer) {
            buffer.writeByte(ordinal());
        }
    }
}