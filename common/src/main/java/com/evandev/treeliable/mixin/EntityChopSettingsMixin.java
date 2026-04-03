package com.evandev.treeliable.mixin;

import com.evandev.treeliable.common.settings.ChopSettings;
import com.evandev.treeliable.common.settings.ChoppingEntity;
import com.evandev.treeliable.common.settings.SyncedChopData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityChopSettingsMixin implements ChoppingEntity {
    private final String KEY = "treeliable:chopSettings";
    private SyncedChopData chopSettings;

    @Override
    public SyncedChopData getChopData() {
        return chopSettings;
    }

    @Override
    public void setChopData(SyncedChopData chopSettings) {
        this.chopSettings = chopSettings;
    }

    @Inject(method = "saveWithoutId", at = @At("HEAD"))
    public void injectDataSaving(ValueOutput output, CallbackInfo info) {
        if (chopSettings != null) {
            output.store(KEY, CompoundTag.CODEC, chopSettings.makeSaveData());
        }
    }

    @Inject(method = "load", at = @At("HEAD"))
    public void injectDataLoading(ValueInput input, CallbackInfo info) {
        input.read(KEY, CompoundTag.CODEC).ifPresent(data -> {
            chopSettings = (new SyncedChopData(new ChopSettings())).readSaveData(data);
        });
    }
}