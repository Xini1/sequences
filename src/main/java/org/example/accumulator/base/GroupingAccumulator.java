package org.example.accumulator.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Maxim Tereshchenko
 */
public final class GroupingAccumulator<T, K> implements Accumulator<T, Map<K, List<T>>> {

    private final Accumulator<T, Map<K, List<T>>> original;

    public GroupingAccumulator(Function<T, K> classifier) {
        original = new MapAccumulator<>(classifier, List::of, this::combine);
    }

    @Override
    public Accumulator<T, Map<K, List<T>>> onElement(T element) {
        return original.onElement(element);
    }

    @Override
    public Map<K, List<T>> onFinish() {
        return original.onFinish();
    }

    private List<T> combine(List<T> first, List<T> second) {
        var buffer = new ArrayList<>(first);
        buffer.addAll(second);

        return List.copyOf(buffer);
    }
}
