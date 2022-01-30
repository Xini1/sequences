package org.example.accumulator.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Maxim Tereshchenko
 */
public final class ListAccumulator<T> implements Accumulator<T, List<T>> {

    private final Accumulator<T, Collection<T>> original;

    private ListAccumulator(Accumulator<T, Collection<T>> original) {
        this.original = original;
    }

    public ListAccumulator() {
        this(new CollectionAccumulator<>(ArrayList::new));
    }

    @Override
    public boolean canAccept() {
        return original.canAccept();
    }

    @Override
    public Accumulator<T, List<T>> onElement(T element) {
        return new ListAccumulator<>(original.onElement(element));
    }

    @Override
    public List<T> onFinish() {
        return List.copyOf(original.onFinish());
    }
}
