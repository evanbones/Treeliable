package com.evandev.treeliable.common.config.resource;

import net.minecraft.core.DefaultedRegistry;

import java.util.stream.Stream;

public class MalformedResourceIdentifier extends ResourceIdentifier {

    public MalformedResourceIdentifier(String string, String explanation) {
        super("", "", string);
        parsingError(explanation);
    }

    @Override
    public <R extends DefaultedRegistry<T>, T> Stream<T> resolve(R registry) {
        return Stream.empty();
    }
}
