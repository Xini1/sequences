package org.example.accumulator.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Maxim Tereshchenko
 */
public final class GroupingAccumulator<T, K> implements Accumulator<T, Map<K, List<T>>> {

    private final Function<T, K> classifier;
    private final Map<K, List<T>> map;

    private GroupingAccumulator(Function<T, K> classifier, Map<K, List<T>> map) {
        this.classifier = classifier;
        this.map = Collections.unmodifiableMap(map);
    }

    public GroupingAccumulator(Function<T, K> classifier) {
        this(classifier, new HashMap<>());
    }

    @Override
    public Accumulator<T, Map<K, List<T>>> onElement(T element) {
        var copy = new HashMap<>(map);
        copy.computeIfAbsent(classifier.apply(element), unused -> new ArrayList<>())
                .add(element);

        return new GroupingAccumulator<>(classifier, copy);
    }

    @Override
    public Map<K, List<T>> onFinish() {
        return Map.copyOf(map);
    }
}
