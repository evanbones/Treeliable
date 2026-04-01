package com.evandev.treeliable.common.network;

import com.evandev.treeliable.Treeliable;
import com.evandev.treeliable.client.Client;
import com.evandev.treeliable.common.settings.Permissions;
import com.evandev.treeliable.common.settings.Setting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ServerPermissionsPacket {
    public static final ResourceLocation ID = Treeliable.resource("server_permissions");

    private final Permissions permissions;

    public ServerPermissionsPacket(Permissions permissions) {
        this.permissions = permissions;
    }

    public static ServerPermissionsPacket decode(FriendlyByteBuf buffer) {
        int numSettings = buffer.readInt();
        List<Setting> settings = IntStream.range(0, numSettings)
                .mapToObj($ -> Setting.decode(buffer))
                .collect(Collectors.toList());

        return new ServerPermissionsPacket(new Permissions(settings));
    }

    public void encode(FriendlyByteBuf buffer) {
        Set<Setting> settings = permissions.getPermittedSettings();
        buffer.writeInt(settings.size());
        settings.forEach(setting -> setting.encode(buffer));
    }

    public void handle() {
        Client.updatePermissions(permissions);
    }
}