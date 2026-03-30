package com.evandev.treeliable.common.config;

public enum ListType {
    BLACKLIST(true),
    WHITELIST(false);

    private final boolean xor;

    ListType(boolean xor) {
        this.xor = xor;
    }

    public boolean accepts(boolean truth) {
        return truth ^ xor;
    }
}
