package org.example.accumulator;

import org.example.accumulator.base.Accumulator;

/**
 * @author Maxim Tereshchenko
 */
public final class SkippingAccumulator<T, R> implements Accumulator<T, R> {

    private final Accumulator<T, R> original;
    private final long count;
    private final long skipped;

    private SkippingAccumulator(Accumulator<T, R> original, long count, long skipped) {
        this.original = original;
        this.count = count;
        this.skipped = skipped;
    }

    public SkippingAccumulator(Accumulator<T, R> original, long count) {
        this(original, count, 0);
    }

    @Override
    public Accumulator<T, R> onElement(T element) {
        if (skipped < count) {
            return new SkippingAccumulator<>(original, count, skipped + 1);
        }

        return original.onElement(element);
    }

    @Override
    public R onFinish() {
        return original.onFinish();
    }
}
