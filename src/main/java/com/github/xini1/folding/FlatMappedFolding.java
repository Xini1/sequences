package com.github.xini1.folding;

import com.github.xini1.accumulator.FlatMappingAccumulator;
import com.github.xini1.accumulator.base.Accumulator;

/**
 * @author Maxim Tereshchenko
 */
public final class FlatMappedFolding<T, F extends Folding<T>> implements Folding<T> {

    private final Folding<F> original;

    public FlatMappedFolding(Folding<F> original) {
        this.original = original;
    }

    @Override
    public <R> R fold(Accumulator<T, R> accumulator) {
        return original.fold(new FlatMappingAccumulator<>(accumulator));
    }
}
