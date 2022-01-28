package org.example.accumulator;

import org.example.accumulator.base.Accumulator;
import org.example.folding.Folding;

/**
 * @author Maxim Tereshchenko
 */
public final class FlatMappingAccumulator<T, F extends Folding<T>, R> implements Accumulator<F, R> {

    private final Accumulator<T, R> original;

    public FlatMappingAccumulator(Accumulator<T, R> original) {
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
