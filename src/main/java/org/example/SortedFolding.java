package org.example;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author Maxim Tereshchenko
 */
class SortedFolding<T> implements Folding<T> {

    private final Folding<T> folding;
    private final Comparator<T> comparator;

    SortedFolding(Folding<T> folding, Comparator<T> comparator) {
        this.folding = folding;
        this.comparator = comparator;
    }

    @Override
    public <R> R fold(Accumulator<T, R> accumulator) {
        return folding.fold(new SortingAccumulator<>(accumulator, comparator));
    }

    private static final class SortingAccumulator<T, R> implements Accumulator<T, R> {

        private final Accumulator<T, R> original;
        private final Comparator<T> comparator;
        private final Collection<T> buffer;

        private SortingAccumulator(Accumulator<T, R> original, Comparator<T> comparator, Collection<T> buffer) {
            this.original = original;
            this.comparator = comparator;
            this.buffer = Collections.unmodifiableCollection(buffer);
        }

        private SortingAccumulator(Accumulator<T, R> original, Comparator<T> comparator) {
            this(original, comparator, new ArrayList<>());
        }

        @Override
        public Accumulator<T, R> onElement(T element) {
            var copy = new ArrayList<>(buffer);
            copy.add(element);
            copy.sort(comparator);

            return new SortingAccumulator<>(original, comparator, copy);
        }

        @Override
        public R onFinish() {
            var current = original;

            for (T element : buffer) {
                current = current.onElement(element);
            }

            return current.onFinish();
        }
    }
}
