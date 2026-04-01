package com.evandev.treeliable.common.network;

import com.evandev.treeliable.Treeliable;
import com.evandev.treeliable.client.Client;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ServerConfirmSettingsPacket {
    public static final ResourceLocation ID = Treeliable.resource("server_confirm_settings");

    private final List<ConfirmedSetting> settings;

    public ServerConfirmSettingsPacket(final List<ConfirmedSetting> settings) {
        this.settings = settings;
    }

    public static ServerConfirmSettingsPacket decode(FriendlyByteBuf buffer) {
        int numSettings = buffer.readInt();
        List<ConfirmedSetting> settings = IntStream.range(0, numSettings)
                .mapToObj($ -> ConfirmedSetting.decode(buffer))
                .collect(Collectors.toList());

        return new ServerConfirmSettingsPacket(settings);
    }

    private static void processSingleSetting(ConfirmedSetting setting) {
        Client.getChopSettings().accept(setting.getField(), setting.getValue());
        setting.event.run(setting);
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(settings.size());
        settings.forEach(setting -> setting.encode(buffer));
    }

    public void handle() {
        settings.forEach(ServerConfirmSettingsPacket::processSingleSetting);
    }
}