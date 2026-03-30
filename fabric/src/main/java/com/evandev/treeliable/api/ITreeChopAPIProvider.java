package com.evandev.treeliable.api;

@FunctionalInterface
public interface ITreeChopAPIProvider {
    TreeChopAPI get(String modId);
}
