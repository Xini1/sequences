package org.example;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Maxim Tereshchenko
 */
class DistinctFolding<T> implements Folding<T> {

    private final Folding<T> original;

    DistinctFolding(Folding<T> original) {
        this.original = original;
    }

    @Override
    public <R> R fold(Accumulator<T, R> accumulator) {
        return original.fold(new DistinctAccumulator<>(accumulator));
    }

    private static final class DistinctAccumulator<T, R> implements Accumulator<T, R> {

        private final Accumulator<T, R> original;
        private final Set<T> set;

        private DistinctAccumulator(Accumulator<T, R> original, Set<T> set) {
            this.original = original;
            this.set = Collections.unmodifiableSet(set);
        }

        private DistinctAccumulator(Accumulator<T, R> original) {
            this(original, new HashSet<>());
        }

        @Override
        public Accumulator<T, R> onElement(T element) {
            if (set.contains(element)) {
                return new DistinctAccumulator<>(original, set);
            }

            var copy = new HashSet<>(set);
            copy.add(element);

            return new DistinctAccumulator<>(original.onElement(element), copy);
        }

        @Override
        public R onFinish() {
            return original.onFinish();
        }
    }
}
