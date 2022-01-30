package com.github.xini1.folding;

import com.github.xini1.accumulator.DistinctAccumulator;
import com.github.xini1.accumulator.base.Accumulator;

/**
 * @author Maxim Tereshchenko
 */
public final class DistinctFolding<T> implements Folding<T> {

    private final Folding<T> original;

    public DistinctFolding(Folding<T> original) {
        this.original = original;
    }

    @Override
    public <R> R fold(Accumulator<T, R> accumulator) {
        return original.fold(new DistinctAccumulator<>(accumulator));
    }
}
