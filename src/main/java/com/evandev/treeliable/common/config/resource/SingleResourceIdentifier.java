package com.evandev.treeliable.common.config.resource;

import net.minecraft.core.DefaultedRegistry;
//? if >=26.1
//import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.stream.Stream;

public class SingleResourceIdentifier extends ResourceIdentifier {

    public SingleResourceIdentifier(String nameSpace, String localSpace, List<IdentifierQualifier> qualifiers, String string) {
        super(nameSpace, localSpace, string);
    }

    @Override
    public <R extends DefaultedRegistry<T>, T> Stream<T> resolve(R registry) {
        String resourceString = getNamespace() + ":" + getLocalSpace();
        ResourceLocation key = ResourceLocation.tryParse(resourceString);

        if (key != null) {
            //? if >=26.1 {
            /*return registry.get(key)
                    .map(Holder.Reference::value)
                    .filter(resource -> {
                        ResourceLocation defaultKey = registry.getDefaultKey();
                        ResourceLocation foundKey = registry.getKey(resource);

                        return !foundKey.equals(defaultKey) || key.equals(defaultKey);
                    })
                    .stream();
            *///?} else {
            if (registry.containsKey(key)) {
                T resource = registry.get(key);
                ResourceLocation defaultKey = registry.getDefaultKey();
                if (!registry.getKey(resource).equals(defaultKey) || key.equals(defaultKey)) {
                    return Stream.of(resource);
                }
            }
            return Stream.empty();
            //?}
        } else {
            parsingError(String.format("\"%s\" is not a valid resource location", getResourceLocation()));
            return Stream.empty();
        }
    }

}
