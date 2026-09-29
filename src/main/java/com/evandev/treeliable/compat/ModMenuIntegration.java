package com.evandev.treeliable.compat;

//? if fabric {
import com.evandev.treeliable.client.integration.YaclConfigIntegration;
import com.evandev.treeliable.platform.Services;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (!Services.PLATFORM.isModLoaded("yet_another_config_lib_v3")) {
            return null;
        }

        return YaclConfigIntegration::createScreen;
    }
}
//?}
