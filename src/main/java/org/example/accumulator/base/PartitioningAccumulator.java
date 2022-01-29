package org.example.accumulator.base;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author Maxim Tereshchenko
 */
public final class PartitioningAccumulator<T> implements Accumulator<T, Map<Boolean, List<T>>> {

    private final Accumulator<T, Map<Boolean, List<T>>> original;

    public PartitioningAccumulator(Predicate<T> predicate) {
        original = new GroupingAccumulator<>(predicate::test);
    }

    @Override
    public Accumulator<T, Map<Boolean, List<T>>> onElement(T element) {
        return original.onElement(element);
    }

    @Override
    public Map<Boolean, List<T>> onFinish() {
        return original.onFinish();
    }
}
