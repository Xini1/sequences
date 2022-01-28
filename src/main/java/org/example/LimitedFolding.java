package org.example;

/**
 * @author Maxim Tereshchenko
 */
class LimitedFolding<T> implements Folding<T> {

    private final Folding<T> original;
    private final long size;

    LimitedFolding(Folding<T> original, long size) {
        this.original = original;
        this.size = size;
    }

    @Override
    public <R> R fold(Accumulator<T, R> accumulator) {
        return original.fold(new LimitingAccumulator<>(accumulator, size));
    }

    private static final class LimitingAccumulator<T, R> implements Accumulator<T, R> {

        private final Accumulator<T, R> original;
        private final long maxSize;
        private final long currentSize;

        private LimitingAccumulator(Accumulator<T, R> original, long maxSize, long currentSize) {
            this.original = original;
            this.maxSize = maxSize;
            this.currentSize = currentSize;
        }

        private LimitingAccumulator(Accumulator<T, R> original, long maxSize) {
            this(original, maxSize, 0);
        }

        @Override
        public Accumulator<T, R> onElement(T element) {
            return new LimitingAccumulator<>(accumulator(element), maxSize, currentSize + 1);
        }

        @Override
        public R onFinish() {
            return original.onFinish();
        }

        private Accumulator<T, R> accumulator(T element) {
            if (currentSize >= maxSize) {
                return original;
            }

            return original.onElement(element);
        }
    }
}
