package com.github.xini1.accumulator;

import com.github.xini1.accumulator.base.Accumulator;

import java.util.function.Predicate;

/**
 * @author Maxim Tereshchenko
 */
abstract class BaseTakingWhilePredicateSatisfiedAccumulator<T, R> implements Accumulator<T, R> {

    private final Accumulator<T, R> original;
    private final Predicate<T> predicate;

    BaseTakingWhilePredicateSatisfiedAccumulator(Accumulator<T, R> original, Predicate<T> predicate) {
        this.original = original;
        this.predicate = predicate;
    }

    @Override
    public boolean canAccept() {
        return original.canAccept();
    }

    @Override
    public Accumulator<T, R> onElement(T element) {
        if (predicate.test(element)) {
            return nextAccumulatorOnPositivePredicate(original.onElement(element), predicate);
        }

        return new IgnoringAccumulator<>(original);
    }

    @Override
    public R onFinish() {
        return original.onFinish();
    }

    abstract Accumulator<T, R> nextAccumulatorOnPositivePredicate(
            Accumulator<T, R> nextOriginalAccumulator,
            Predicate<T> predicate
    );

    private static final class IgnoringAccumulator<T, R> implements Accumulator<T, R> {

        private final Accumulator<T, R> original;

        private IgnoringAccumulator(Accumulator<T, R> original) {
            this.original = original;
        }

        @Override
        public boolean canAccept() {
            return false;
        }

        @Override
        public Accumulator<T, R> onElement(T element) {
            return this;
        }

        @Override
        public R onFinish() {
            return original.onFinish();
        }
    }
}
