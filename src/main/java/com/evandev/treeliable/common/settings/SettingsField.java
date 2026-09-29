package com.evandev.treeliable.common.settings;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Set;

public enum SettingsField {
    CHOPPING("choppingEnabled", "treeliable.setting.chopping", Boolean.TRUE),
    SNEAK_BEHAVIOR("sneakBehavior", "treeliable.setting.sneak_behavior", SneakBehavior.INVERT_CHOPPING),
    TREES_MUST_HAVE_LEAVES("treeMustHaveLeaves", "treeliable.setting.trees_must_have_leaves", Boolean.TRUE),
    CHOP_IN_CREATIVE_MODE("chopInCreativeMode", "treeliable.setting.chop_in_creative_mode", Boolean.FALSE);

    public static final SettingsField[] VALUES = values();

    private final String configKey;
    private final String langKey;
    private final Object defaultValue;

    SettingsField(String configKey, String langKey, Object defaultValue) {
        this.configKey = configKey;
        this.langKey = langKey;
        this.defaultValue = defaultValue;
    }

    public static Setting decode(FriendlyByteBuf buffer) {
        SettingsField field = SettingsField.values()[buffer.readByte()];
        Object value = field.decodeValue(buffer);
        return new Setting(field, value);
    }

    public String getConfigKey() {
        return configKey;
    }

    public String getFancyName() {
        return I18n.get(langKey);
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void encode(FriendlyByteBuf buffer, Object value) {
        buffer.writeByte(ordinal());
        if (value instanceof Boolean bool) {
            buffer.writeBoolean(bool);
        } else if (value instanceof SneakBehavior sneakBehavior) {
            buffer.writeEnum(sneakBehavior);
        }
    }

    private Object decodeValue(FriendlyByteBuf buffer) {
        if (this == SNEAK_BEHAVIOR) {
            return buffer.readEnum(SneakBehavior.class);
        } else {
            return buffer.readBoolean();
        }
    }

    public String getValueName(Object value) {
        if (value instanceof Boolean bool) {
            return I18n.get(bool ? "treeliable.setting.state.on" : "treeliable.setting.state.off");
        } else if (value instanceof SneakBehavior sneakBehavior) {
            return sneakBehavior.getFancyText();
        }
        return value.toString();
    }

    public Set<Object> getValues() {
        if (this == SNEAK_BEHAVIOR) return Set.of(SneakBehavior.values());
        return Set.of(Boolean.TRUE, Boolean.FALSE);
    }
}