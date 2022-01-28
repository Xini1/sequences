package org.example.accumulator;

import org.example.accumulator.base.Accumulator;

/**
 * @author Maxim Tereshchenko
 */
public final class LimitingAccumulator<T, R> implements Accumulator<T, R> {

    private final Accumulator<T, R> original;
    private final long maxSize;
    private final long currentSize;

    private LimitingAccumulator(Accumulator<T, R> original, long maxSize, long currentSize) {
        this.original = original;
        this.maxSize = maxSize;
        this.currentSize = currentSize;
    }

    public LimitingAccumulator(Accumulator<T, R> original, long maxSize) {
        this(original, maxSize, 0);
    }

    @Override
    public Accumulator<T, R> onElement(T element) {
        if (currentSize < maxSize) {
            return new LimitingAccumulator<>(original.onElement(element), maxSize, currentSize + 1);
        }

        return new FullLimitingAccumulator<>(original);
    }

    @Override
    public R onFinish() {
        return original.onFinish();
    }

    private static final class FullLimitingAccumulator<T, R> implements Accumulator<T, R> {

        private final Accumulator<T, R> original;

        private FullLimitingAccumulator(Accumulator<T, R> original) {
            this.original = original;
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
