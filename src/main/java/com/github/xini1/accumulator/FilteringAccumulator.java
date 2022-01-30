package com.github.xini1.accumulator;

import com.github.xini1.accumulator.base.Accumulator;

import java.util.function.Predicate;

/**
 * @author Maxim Tereshchenko
 */
public final class FilteringAccumulator<T, R> implements Accumulator<T, R> {

    private final Accumulator<T, R> original;
    private final Predicate<T> predicate;

    public FilteringAccumulator(Accumulator<T, R> original, Predicate<T> predicate) {
        this.original = original;
        this.predicate = predicate;
    }

    @Override
    public boolean canAccept() {
        return original.canAccept();
    }

    @Override
    public Accumulator<T, R> onElement(T element) {
        if (!predicate.test(element)) {
            return this;
        }

        return new FilteringAccumulator<>(original.onElement(element), predicate);
    }

    @Override
    public R onFinish() {
        return original.onFinish();
    }
}
