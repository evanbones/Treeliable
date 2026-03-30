package com.evandev.treeliable.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.evandev.treeliable.Treeliable;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class KeyBindings {
    public static final String CATEGORY = "HT's Treeliable";

    public static final List<ActionableKeyBinding> allKeyBindings = new LinkedList<>();

    public static void registerKeyMappings(Consumer<KeyMapping> register) {
        registerKeyBinding("toggle_chopping", InputConstants.UNKNOWN, Client::toggleChopping, register);
        registerKeyBinding("cycle_sneak_behavior", InputConstants.UNKNOWN, Client::cycleSneakBehavior, register);
    }

    private static ActionableKeyBinding registerKeyBinding(String name, InputConstants.Key defaultKey, Runnable callback, Consumer<KeyMapping> register) {
        ActionableKeyBinding keyBinding = new ActionableKeyBinding(
                String.format("%s.key.%s", Treeliable.MOD_ID, name),
                defaultKey,
                callback
        );
        register.accept(keyBinding);

        allKeyBindings.add(keyBinding);

        return keyBinding;
    }

    public static class ActionableKeyBinding extends KeyMapping {

        private final Runnable callback;

        public ActionableKeyBinding(String resourceName, InputConstants.Key inputByCode, Runnable callback) {
            super(resourceName, InputConstants.Type.KEYSYM, inputByCode.getValue(), CATEGORY);
            this.callback = () -> {
                Screen screen = Minecraft.getInstance().screen;
                if (screen == null) {
                    callback.run();
                }
            };
        }

        public void onPress() {
            callback.run();
        }

    }
}
