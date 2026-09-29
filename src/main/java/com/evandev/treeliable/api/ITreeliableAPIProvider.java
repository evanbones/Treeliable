package com.evandev.treeliable.api;

//? if fabric {
@FunctionalInterface
public interface ITreeliableAPIProvider {
    TreeliableAPI get(String modId);
}
//?}
