package org.example.accumulator.base;

import java.util.function.BinaryOperator;

/**
 * @author Maxim Tereshchenko
 */
public final class ReducingAccumulator<T> implements Accumulator<T, T> {

    private final T identity;
    private final BinaryOperator<T> binaryOperator;

    public ReducingAccumulator(T identity, BinaryOperator<T> binaryOperator) {
        this.identity = identity;
        this.binaryOperator = binaryOperator;
    }

    @Override
    public Accumulator<T, T> onElement(T element) {
        return new ReducingAccumulator<>(binaryOperator.apply(identity, element), binaryOperator);
    }

    @Override
    public T onFinish() {
        return identity;
    }
}
