package org.example.accumulator;

import org.example.accumulator.base.Accumulator;

import java.util.Optional;

/**
 * @author Maxim Tereshchenko
 */
public final class InitialFindingFirstAccumulator<T> implements Accumulator<T, Optional<T>> {

    @Override
    public boolean canAccept() {
        return true;
    }

    @Override
    public Accumulator<T, Optional<T>> onElement(T element) {
        return new FoundFirstAccumulator<>(element);
    }

    @Override
    public Optional<T> onFinish() {
        return Optional.empty();
    }

    private static final class FoundFirstAccumulator<T> implements Accumulator<T, Optional<T>> {

        private final T first;

        private FoundFirstAccumulator(T first) {
            this.first = first;
        }

        @Override
        public boolean canAccept() {
            return false;
        }

        @Override
        public Accumulator<T, Optional<T>> onElement(T element) {
            return this;
        }

        @Override
        public Optional<T> onFinish() {
            return Optional.of(first);
        }
    }
}
