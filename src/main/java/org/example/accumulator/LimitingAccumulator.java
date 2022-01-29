package org.example.accumulator;

import org.example.accumulator.base.Accumulator;

import java.util.function.Predicate;

/**
 * @author Maxim Tereshchenko
 */
public final class LimitingAccumulator<T, R> extends BaseTakingWhilePredicateSatisfiedAccumulator<T, R> {

    private final long maxSize;
    private final long currentSize;

    private LimitingAccumulator(Accumulator<T, R> original, long maxSize, long currentSize) {
        super(original, unused -> currentSize < maxSize);
        this.maxSize = maxSize;
        this.currentSize = currentSize;
    }

    public LimitingAccumulator(Accumulator<T, R> original, long maxSize) {
        this(original, maxSize, 0);
    }

    @Override
    Accumulator<T, R> nextAccumulator(Accumulator<T, R> nextOriginalAccumulator, Predicate<T> predicate) {
        return new LimitingAccumulator<>(nextOriginalAccumulator, maxSize, currentSize + 1);
    }
}
