package com.evandev.treeliable.client.settings;

import com.evandev.treeliable.client.Client;
import com.evandev.treeliable.common.settings.ChopSettings;
import com.evandev.treeliable.common.settings.SettingsField;
import net.minecraft.client.Minecraft;

public class ClientChopSettings extends ChopSettings {

    @Override
    public ChopSettings set(SettingsField field, Object value) {
        if (Minecraft.getInstance().getConnection() == null) {
            super.set(field, value);
        } else if (!get(field).equals(value)) {
            Client.requestSetting(field, value);
        }
        return this;
    }

    public void accept(SettingsField field, Object value) {
        super.set(field, value);
    }

}
