package com.evandev.treeliable.common.network;

import com.evandev.treeliable.Treeliable;
import com.evandev.treeliable.client.Client;
import com.evandev.treeliable.common.settings.Permissions;
import com.evandev.treeliable.common.settings.Setting;
import net.minecraft.network.FriendlyByteBuf;
//? if >=1.20.5 {
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//?}
import net.minecraft.resources.ResourceLocation;
//? if >=1.20.5
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ServerPermissionsPacket /*? if >=1.20.5 {*/implements CustomPacketPayload/*?}*/ {
    public static final ResourceLocation ID = Treeliable.resource("server_permissions");
    //? if >=1.20.5 {
    public static final CustomPacketPayload.Type<ServerPermissionsPacket> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<FriendlyByteBuf, ServerPermissionsPacket> STREAM_CODEC = CustomPacketPayload.codec(
            ServerPermissionsPacket::encode, ServerPermissionsPacket::decode
    );
    //?}
    private final Permissions permissions;

    public ServerPermissionsPacket(Permissions permissions) {
        this.permissions = permissions;
    }

    public void encode(FriendlyByteBuf buffer) {
        Set<Setting> settings = permissions.getPermittedSettings();
        buffer.writeInt(settings.size());
        settings.forEach(setting -> setting.encode(buffer));
    }

    public static ServerPermissionsPacket decode(FriendlyByteBuf buffer) {
        int numSettings = buffer.readInt();
        List<Setting> settings = IntStream.range(0, numSettings)
                .mapToObj($ -> Setting.decode(buffer))
                .collect(Collectors.toList());

        return new ServerPermissionsPacket(new Permissions(settings));
    }

    public void handle() {
        Client.updatePermissions(permissions);
    }

    //? if >=1.20.5 {
    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    //?}
}
