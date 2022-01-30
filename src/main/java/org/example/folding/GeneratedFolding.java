package org.example.folding;

import org.example.accumulator.base.Accumulator;

import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * @author Maxim Tereshchenko
 */
public final class GeneratedFolding<T> implements Folding<T> {

    private final Supplier<T> seed;
    private final Predicate<T> hasNext;
    private final UnaryOperator<T> next;

    public GeneratedFolding(Supplier<T> seed, Predicate<T> hasNext, UnaryOperator<T> next) {
        this.seed = seed;
        this.hasNext = hasNext;
        this.next = next;
    }

    @Override
    public <R> R fold(Accumulator<T, R> accumulator) {
        var currentElement = seed.get();
        var currentAccumulator = accumulator;

        while (hasNext.test(currentElement) && currentAccumulator.canAccept()) {
            currentAccumulator = currentAccumulator.onElement(currentElement);
            currentElement = next.apply(currentElement);
        }

        return currentAccumulator.onFinish();
    }
}
