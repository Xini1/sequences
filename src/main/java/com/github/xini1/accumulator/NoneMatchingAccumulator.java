package com.github.xini1.accumulator;

import com.github.xini1.accumulator.base.Accumulator;

import java.util.function.Predicate;

/**
 * @author Maxim Tereshchenko
 */
public final class NoneMatchingAccumulator<T> implements Accumulator<T, Boolean> {

    private final Predicate<T> predicate;

    public NoneMatchingAccumulator(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean canAccept() {
        return true;
    }

    @Override
    public Accumulator<T, Boolean> onElement(T element) {
        if (predicate.test(element)) {
            return new OneMatchedAccumulator<>();
        }

        return this;
    }

    @Override
    public Boolean onFinish() {
        return Boolean.TRUE;
    }

    private static final class OneMatchedAccumulator<T> implements Accumulator<T, Boolean> {
        @Override
        public boolean canAccept() {
            return false;
        }

        @Override
        public Accumulator<T, Boolean> onElement(T element) {
            return this;
        }

        @Override
        public Boolean onFinish() {
            return Boolean.FALSE;
        }
    }
}
