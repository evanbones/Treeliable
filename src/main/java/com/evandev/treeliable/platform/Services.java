package com.evandev.treeliable.platform;

import com.evandev.treeliable.Treeliable;
import com.evandev.treeliable.platform.services.IPlatformHelper;

import java.util.ServiceLoader;

public class Services {

    //? if fabric {
    public static final IPlatformHelper PLATFORM = new FabricPlatformHelper();
    //?} else if forge {
    //public static final IPlatformHelper PLATFORM = new ForgePlatformHelper();
    //?} else {
    //public static final IPlatformHelper PLATFORM = new NeoForgePlatformHelper();
    //?}
}
