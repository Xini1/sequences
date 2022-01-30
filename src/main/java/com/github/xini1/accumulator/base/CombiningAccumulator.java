package com.github.xini1.accumulator.base;

import java.util.Optional;
import java.util.function.BinaryOperator;

/**
 * @author Maxim Tereshchenko
 */
public final class CombiningAccumulator<T> implements Accumulator<T, Optional<T>> {

    private final BinaryOperator<T> binaryOperator;

    public CombiningAccumulator(BinaryOperator<T> binaryOperator) {
        this.binaryOperator = binaryOperator;
    }

    @Override
    public boolean canAccept() {
        return true;
    }

    @Override
    public Accumulator<T, Optional<T>> onElement(T element) {
        return new SingleElementCombiningAccumulator<>(element, binaryOperator);
    }

    @Override
    public Optional<T> onFinish() {
        return Optional.empty();
    }

    private static final class SingleElementCombiningAccumulator<T> implements Accumulator<T, Optional<T>> {

        private final T element;
        private final BinaryOperator<T> binaryOperator;

        private SingleElementCombiningAccumulator(T element, BinaryOperator<T> binaryOperator) {
            this.element = element;
            this.binaryOperator = binaryOperator;
        }

        @Override
        public boolean canAccept() {
            return true;
        }

        @Override
        public Accumulator<T, Optional<T>> onElement(T element) {
            return new MultipleElementsCombiningAccumulator<>(
                    binaryOperator.apply(this.element, element),
                    binaryOperator
            );
        }

        @Override
        public Optional<T> onFinish() {
            return Optional.of(element);
        }
    }

    private static final class MultipleElementsCombiningAccumulator<T> implements Accumulator<T, Optional<T>> {

        private final T combined;
        private final BinaryOperator<T> binaryOperator;

        private MultipleElementsCombiningAccumulator(T combined, BinaryOperator<T> binaryOperator) {
            this.combined = combined;
            this.binaryOperator = binaryOperator;
        }

        @Override
        public boolean canAccept() {
            return true;
        }

        @Override
        public Accumulator<T, Optional<T>> onElement(T element) {
            return new MultipleElementsCombiningAccumulator<>(binaryOperator.apply(combined, element), binaryOperator);
        }

        @Override
        public Optional<T> onFinish() {
            return Optional.of(combined);
        }
    }
}
