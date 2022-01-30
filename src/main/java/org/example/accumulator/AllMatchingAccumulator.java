package org.example.accumulator;

import org.example.accumulator.base.Accumulator;

import java.util.function.Predicate;

/**
 * @author Maxim Tereshchenko
 */
public final class AllMatchingAccumulator<T> implements Accumulator<T, Boolean> {

    private final Predicate<T> predicate;

    public AllMatchingAccumulator(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean canAccept() {
        return true;
    }

    @Override
    public Accumulator<T, Boolean> onElement(T element) {
        if (predicate.test(element)) {
            return this;
        }

        return new NotAllMatchedAccumulator<>();
    }

    @Override
    public Boolean onFinish() {
        return Boolean.TRUE;
    }

    private static final class NotAllMatchedAccumulator<T> implements Accumulator<T, Boolean> {

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
