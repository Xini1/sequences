package com.github.xini1.folding;

import com.github.xini1.accumulator.SkippingAccumulator;
import com.github.xini1.accumulator.base.Accumulator;

/**
 * @author Maxim Tereshchenko
 */
public final class FoldingWithSkippedElements<T> implements Folding<T> {

    private final Folding<T> original;
    private final long count;

    public FoldingWithSkippedElements(Folding<T> original, long count) {
        this.original = original;
        this.count = count;
    }

    @Override
    public <R> R fold(Accumulator<T, R> accumulator) {
        return original.fold(new SkippingAccumulator<>(accumulator, count));
    }
}
