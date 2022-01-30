package org.example.accumulator;

import org.example.accumulator.base.Accumulator;

import java.util.function.Predicate;

/**
 * @author Maxim Tereshchenko
 */
abstract class BaseDroppingWhilePredicateSatisfiedAccumulator<T, R> implements Accumulator<T, R> {

    private final Accumulator<T, R> original;
    private final Predicate<T> predicate;

    BaseDroppingWhilePredicateSatisfiedAccumulator(Accumulator<T, R> original, Predicate<T> predicate) {
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
            return nextAccumulatorOnPositivePredicate(original, predicate);
        }

        return original.onElement(element);
    }

    @Override
    public R onFinish() {
        return original.onFinish();
    }

    abstract Accumulator<T, R> nextAccumulatorOnPositivePredicate(Accumulator<T, R> original, Predicate<T> predicate);
}
