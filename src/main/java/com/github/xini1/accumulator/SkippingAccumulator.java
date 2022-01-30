package com.github.xini1.accumulator;

import com.github.xini1.accumulator.base.Accumulator;

import java.util.function.Predicate;

/**
 * @author Maxim Tereshchenko
 */
public final class SkippingAccumulator<T, R> extends BaseDroppingWhilePredicateSatisfiedAccumulator<T, R> {

    private final long count;
    private final long skipped;

    private SkippingAccumulator(Accumulator<T, R> original, long count, long skipped) {
        super(original, unused -> skipped < count);
        this.count = count;
        this.skipped = skipped;
    }

    public SkippingAccumulator(Accumulator<T, R> original, long count) {
        this(original, count, 0);
    }

    @Override
    Accumulator<T, R> nextAccumulatorOnPositivePredicate(Accumulator<T, R> original, Predicate<T> predicate) {
        return new SkippingAccumulator<>(original, count, skipped + 1);
    }
}
