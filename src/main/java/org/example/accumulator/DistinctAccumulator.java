package org.example.accumulator;

import org.example.accumulator.base.Accumulator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Maxim Tereshchenko
 */
public final class DistinctAccumulator<T, R> implements Accumulator<T, R> {

    private final Accumulator<T, R> original;
    private final Set<T> set;

    private DistinctAccumulator(Accumulator<T, R> original, Set<T> set) {
        this.original = original;
        this.set = Collections.unmodifiableSet(set);
    }

    public DistinctAccumulator(Accumulator<T, R> original) {
        this(original, new HashSet<>());
    }

    @Override
    public boolean canAccept() {
        return original.canAccept();
    }

    @Override
    public Accumulator<T, R> onElement(T element) {
        if (set.contains(element)) {
            return this;
        }

        var copy = new HashSet<>(set);
        copy.add(element);

        return new DistinctAccumulator<>(original.onElement(element), copy);
    }

    @Override
    public R onFinish() {
        return original.onFinish();
    }
}
