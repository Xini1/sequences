package org.example;

import java.util.function.Predicate;

/**
 * @author Maxim Tereshchenko
 */
class FilteredFolding<T> implements Folding<T> {

    private final Folding<T> original;
    private final Predicate<T> predicate;

    FilteredFolding(Folding<T> original, Predicate<T> predicate) {
        this.original = original;
        this.predicate = predicate;
    }

    @Override
    public <R> R fold(Accumulator<T, R> accumulator) {
        return original.fold(new FilteringAccumulator<>(accumulator, predicate));
    }

    private static final class FilteringAccumulator<T, R> implements Accumulator<T, R> {

        private final Accumulator<T, R> original;
        private final Predicate<T> predicate;

        private FilteringAccumulator(Accumulator<T, R> original, Predicate<T> predicate) {
            this.original = original;
            this.predicate = predicate;
        }

        @Override
        public Accumulator<T, R> onElement(T element) {
            return new FilteringAccumulator<>(accumulator(element), predicate);
        }

        @Override
        public R onFinish() {
            return original.onFinish();
        }

        private Accumulator<T, R> accumulator(T element) {
            if (!predicate.test(element)) {
                return original;
            }

            return original.onElement(element);
        }
    }
}
