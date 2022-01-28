package org.example;

/**
 * @author Maxim Tereshchenko
 */
class FlatMappedFolding<T, F extends Folding<T>> implements Folding<T> {

    private final Folding<F> original;

    FlatMappedFolding(Folding<F> original) {
        this.original = original;
    }

    @Override
    public <R> R fold(Accumulator<T, R> accumulator) {
        return original.fold(new FlatMappingAccumulator<>(accumulator));
    }

    private static final class FlatMappingAccumulator<T, F extends Folding<T>, R> implements Accumulator<F, R> {

        private final Accumulator<T, R> original;

        private FlatMappingAccumulator(Accumulator<T, R> original) {
            this.original = original;
        }

        @Override
        public Accumulator<F, R> onElement(F element) {
            return new FlatMappingAccumulator<>(element.fold(new ReturningNestedAccumulatorOnFinish<>(original)));
        }

        @Override
        public R onFinish() {
            return original.onFinish();
        }
    }

    private static final class ReturningNestedAccumulatorOnFinish<T, R> implements Accumulator<T, Accumulator<T, R>> {

        private final Accumulator<T, R> original;

        private ReturningNestedAccumulatorOnFinish(Accumulator<T, R> original) {
            this.original = original;
        }

        @Override
        public Accumulator<T, Accumulator<T, R>> onElement(T element) {
            return new ReturningNestedAccumulatorOnFinish<>(original.onElement(element));
        }

        @Override
        public Accumulator<T, R> onFinish() {
            return original;
        }
    }
}
