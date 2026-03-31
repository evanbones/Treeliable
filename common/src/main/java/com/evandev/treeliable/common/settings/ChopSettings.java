package com.evandev.treeliable.common.settings;

import com.evandev.treeliable.Treeliable;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class ChopSettings {

    private final EnumMap<SettingsField, Object> fieldValues = new EnumMap<>(SettingsField.class);

    public ChopSettings() {
        for (SettingsField field : SettingsField.values()) {
            fieldValues.put(field, field.getDefaultValue());
        }
    }

    public ChopSettings(ChopSettings template) {
        this();
        copyFrom(template);
    }

    public boolean getChoppingEnabled() {
        return get(SettingsField.CHOPPING, Boolean.class);
    }

    public void setChoppingEnabled(boolean enabled) {
        set(SettingsField.CHOPPING, enabled);
    }

    public SneakBehavior getSneakBehavior() {
        return get(SettingsField.SNEAK_BEHAVIOR, SneakBehavior.class);
    }

    public void setSneakBehavior(SneakBehavior behavior) {
        set(SettingsField.SNEAK_BEHAVIOR, behavior);
    }

    public boolean getTreesMustHaveLeaves() {
        return get(SettingsField.TREES_MUST_HAVE_LEAVES, Boolean.class);
    }

    public void setTreesMustHaveLeaves(boolean enabled) {
        set(SettingsField.TREES_MUST_HAVE_LEAVES, enabled);
    }

    public boolean getChopInCreativeMode() {
        return get(SettingsField.CHOP_IN_CREATIVE_MODE, Boolean.class);
    }

    public void setChopInCreativeMode(boolean enabled) {
        set(SettingsField.CHOP_IN_CREATIVE_MODE, enabled);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof ChopSettings otherSettings) {
            return Arrays.stream(SettingsField.VALUES)
                    .allMatch(field -> this.get(field).equals(otherSettings.get(field)));
        } else {
            return false;
        }
    }

    public void copyFrom(ChopSettings other) {
        fieldValues.putAll(other.fieldValues);
    }

    public <T> T get(SettingsField field, Class<T> type) {
        Object value = fieldValues.get(field);
        if (!type.isInstance(value)) {
//            TreeliableMod.LOGGER.warn(String.format("SettingsField %s has illegal value %s (%s); reverting to default", field, value, value.getClass()));
            value = field.getDefaultValue();
            fieldValues.put(field, value);
        }

        return type.cast(value);
    }

    public Setting getSetting(SettingsField field) {
        return new Setting(field, get(field));
    }

    public Object get(SettingsField field) {
        return fieldValues.get(field);
    }

    public void forEach(BiConsumer<SettingsField, Object> consumer) {
        fieldValues.forEach(consumer);
    }

    public ChopSettings set(SettingsField field, Object value) {
        if (field.getDefaultValue().getClass().isInstance(value)) {
            fieldValues.put(field, value);
        } else {
            Treeliable.LOGGER.warn("Refusing to set setting {} to illegal value {} ({})", field, value, value.getClass());
        }
        return this;
    }

    public ChopSettings set(Pair<SettingsField, Object> fieldValuePair) {
        return set(fieldValuePair.getLeft(), fieldValuePair.getRight());
    }

    public ChopSettings set(Setting setting) {
        return set(setting.getField(), setting.getValue());
    }

    public List<Setting> getAll() {
        return Arrays.stream(SettingsField.values())
                .map(field -> new Setting(field, get(field)))
                .collect(Collectors.toList());
    }
}
