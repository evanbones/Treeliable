package com.evandev.treeliable.api;

@FunctionalInterface
public interface ITreeliableAPIProvider {
    TreeliableAPI get(String modId);
}
