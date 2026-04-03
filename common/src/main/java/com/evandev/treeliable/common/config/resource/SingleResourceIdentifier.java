package com.evandev.treeliable.common.config.resource;

import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.stream.Stream;

public class SingleResourceIdentifier extends ResourceIdentifier {

    public SingleResourceIdentifier(String nameSpace, String localSpace, List<IdentifierQualifier> qualifiers, String string) {
        super(nameSpace, localSpace, string);
    }

    @Override
    public <R extends DefaultedRegistry<T>, T> Stream<T> resolve(R registry) {
        String resourceString = getNamespace() + ":" + getLocalSpace();
        Identifier key = Identifier.tryParse(resourceString);

        if (key != null) {
            return registry.get(key)
                    .map(Holder.Reference::value)
                    .filter(resource -> {
                        Identifier defaultKey = registry.getDefaultKey();
                        Identifier foundKey = registry.getKey(resource);

                        return !foundKey.equals(defaultKey) || key.equals(defaultKey);
                    })
                    .stream();
        } else {
            parsingError(String.format("\"%s\" is not a valid resource location", getIdentifier()));
            return Stream.empty();
        }
    }
}