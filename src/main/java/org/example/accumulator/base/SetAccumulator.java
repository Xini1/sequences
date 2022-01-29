package org.example.accumulator.base;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Maxim Tereshchenko
 */
public final class SetAccumulator<T> implements Accumulator<T, Set<T>> {

    private final Accumulator<T, Collection<T>> original;

    private SetAccumulator(Accumulator<T, Collection<T>> original) {
        this.original = original;
    }

    public SetAccumulator() {
        this(new CollectionAccumulator<>(HashSet::new));
    }

    @Override
    public Accumulator<T, Set<T>> onElement(T element) {
        return new SetAccumulator<>(original.onElement(element));
    }

    @Override
    public Set<T> onFinish() {
        return Set.copyOf(original.onFinish());
    }
}
