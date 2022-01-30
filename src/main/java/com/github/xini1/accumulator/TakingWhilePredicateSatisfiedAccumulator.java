package com.github.xini1.accumulator;

import com.github.xini1.accumulator.base.Accumulator;

import java.util.function.Predicate;

/**
 * @author Maxim Tereshchenko
 */
public final class TakingWhilePredicateSatisfiedAccumulator<T, R>
        extends BaseTakingWhilePredicateSatisfiedAccumulator<T, R> {

    public TakingWhilePredicateSatisfiedAccumulator(Accumulator<T, R> original, Predicate<T> predicate) {
        super(original, predicate);
    }

    @Override
    Accumulator<T, R> nextAccumulatorOnPositivePredicate(
            Accumulator<T, R> nextOriginalAccumulator,
            Predicate<T> predicate
    ) {
        return new TakingWhilePredicateSatisfiedAccumulator<>(nextOriginalAccumulator, predicate);
    }
}
