package org.example.folding;

import org.example.accumulator.LimitingAccumulator;
import org.example.accumulator.base.Accumulator;

/**
 * @author Maxim Tereshchenko
 */
public final class LimitedFolding<T> implements Folding<T> {

    private final Folding<T> original;
    private final long size;

    public LimitedFolding(Folding<T> original, long size) {
        this.original = original;
        this.size = size;
    }

    @Override
    public <R> R fold(Accumulator<T, R> accumulator) {
        return original.fold(new LimitingAccumulator<>(accumulator, size));
    }
}
