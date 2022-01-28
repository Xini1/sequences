package org.example.folding;

import org.example.accumulator.DistinctAccumulator;
import org.example.accumulator.base.Accumulator;

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
