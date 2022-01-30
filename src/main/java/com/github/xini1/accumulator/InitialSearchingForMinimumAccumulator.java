package com.github.xini1.accumulator;

import com.github.xini1.accumulator.base.Accumulator;

import java.util.Comparator;
import java.util.Optional;

/**
 * @author Maxim Tereshchenko
 */
public final class InitialSearchingForMinimumAccumulator<T> implements Accumulator<T, Optional<T>> {

    private final Comparator<T> comparator;

    public InitialSearchingForMinimumAccumulator(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    @Override
    public boolean canAccept() {
        return true;
    }

    @Override
    public Accumulator<T, Optional<T>> onElement(T element) {
        return new SearchingForMinimumAccumulator<>(comparator, element);
    }

    @Override
    public Optional<T> onFinish() {
        return Optional.empty();
    }

    private static final class SearchingForMinimumAccumulator<T> implements Accumulator<T, Optional<T>> {

        private final Comparator<T> comparator;
        private final T currentMinimum;

        private SearchingForMinimumAccumulator(Comparator<T> comparator, T currentMinimum) {
            this.comparator = comparator;
            this.currentMinimum = currentMinimum;
        }

        @Override
        public boolean canAccept() {
            return true;
        }

        @Override
        public Accumulator<T, Optional<T>> onElement(T element) {
            if (comparator.compare(element, currentMinimum) < 0) {
                return new SearchingForMinimumAccumulator<>(comparator, element);
            }

            return this;
        }

        @Override
        public Optional<T> onFinish() {
            return Optional.of(currentMinimum);
        }
    }
}
