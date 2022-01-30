package com.github.xini1.accumulator;

import com.github.xini1.accumulator.base.Accumulator;

import java.util.function.Predicate;

/**
 * @author Maxim Tereshchenko
 */
public final class DroppingWhilePredicateSatisfiedAccumulator<T, R>
        extends BaseDroppingWhilePredicateSatisfiedAccumulator<T, R> {

    public DroppingWhilePredicateSatisfiedAccumulator(Accumulator<T, R> original, Predicate<T> predicate) {
        super(original, predicate);
    }

    @Override
    Accumulator<T, R> nextAccumulatorOnPositivePredicate(Accumulator<T, R> original, Predicate<T> predicate) {
        return new DroppingWhilePredicateSatisfiedAccumulator<>(original, predicate);
    }
}
