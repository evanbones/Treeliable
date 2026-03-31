package com.evandev.treeliable.common.math.graph;

import java.util.stream.Stream;

public interface DirectedGraph<T> {
    Stream<T> getNeighbors(T node);
}
