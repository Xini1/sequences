package com.github.xini1.folding;

import com.github.xini1.accumulator.FilteringAccumulator;
import com.github.xini1.accumulator.base.Accumulator;

import java.util.function.Predicate;

/**
 * @author Maxim Tereshchenko
 */
public final class FilteredFolding<T> implements Folding<T> {

    private final Folding<T> original;
    private final Predicate<T> predicate;

    public FilteredFolding(Folding<T> original, Predicate<T> predicate) {
        this.original = original;
        this.predicate = predicate;
    }

    @Override
    public <R> R fold(Accumulator<T, R> accumulator) {
        return original.fold(new FilteringAccumulator<>(accumulator, predicate));
    }
}
