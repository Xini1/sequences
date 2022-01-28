package org.example.accumulator;

import org.example.accumulator.base.Accumulator;

/**
 * @author Maxim Tereshchenko
 */
public final class CountingAccumulator<T> implements Accumulator<T, Long> {

    private final long runningTotal;

    private CountingAccumulator(long runningTotal) {
        this.runningTotal = runningTotal;
    }

    public CountingAccumulator() {
        this(0);
    }

    @Override
    public Accumulator<T, Long> onElement(T element) {
        return new CountingAccumulator<>(runningTotal + 1);
    }

    @Override
    public Long onFinish() {
        return runningTotal;
    }
}
