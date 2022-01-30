package com.github.xini1.folding;

import com.github.xini1.accumulator.base.Accumulator;

/**
 * @author Maxim Tereshchenko
 */
public final class IterableFolding<T> implements Folding<T> {

    private final Iterable<T> iterable;

    public IterableFolding(Iterable<T> iterable) {
        this.iterable = iterable;
    }

    @Override
    public <R> R fold(Accumulator<T, R> accumulator) {
        var current = accumulator;

        for (T element : iterable) {
            current = current.onElement(element);
        }

        return current.onFinish();
    }
}
