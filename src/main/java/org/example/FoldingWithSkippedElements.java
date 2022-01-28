package org.example;

/**
 * @author Maxim Tereshchenko
 */
class FoldingWithSkippedElements<T> implements Folding<T> {

    private final Folding<T> original;
    private final long count;

    FoldingWithSkippedElements(Folding<T> original, long count) {
        this.original = original;
        this.count = count;
    }

    @Override
    public <R> R fold(Accumulator<T, R> accumulator) {
        return original.fold(new SkippingAccumulator<>(accumulator, count));
    }

    private static final class SkippingAccumulator<T, R> implements Accumulator<T, R> {

        private final Accumulator<T, R> original;
        private final long count;
        private final long skipped;

        private SkippingAccumulator(Accumulator<T, R> original, long count, long skipped) {
            this.original = original;
            this.count = count;
            this.skipped = skipped;
        }

        private SkippingAccumulator(Accumulator<T, R> original, long count) {
            this(original, count, 0);
        }

        @Override
        public Accumulator<T, R> onElement(T element) {
            return new SkippingAccumulator<>(accumulator(element), count, skipped + 1);
        }

        @Override
        public R onFinish() {
            return original.onFinish();
        }

        private Accumulator<T, R> accumulator(T element) {
            if (skipped < count) {
                return original;
            }

            return original.onElement(element);
        }
    }
}
