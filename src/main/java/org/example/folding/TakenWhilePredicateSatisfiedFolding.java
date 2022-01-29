package org.example.folding;

import org.example.accumulator.TakingWhilePredicateSatisfiedAccumulator;
import org.example.accumulator.base.Accumulator;

import java.util.function.Predicate;

/**
 * @author Maxim Tereshchenko
 */
public final class TakenWhilePredicateSatisfiedFolding<T> implements Folding<T> {

    private final Folding<T> original;
    private final Predicate<T> predicate;

    public TakenWhilePredicateSatisfiedFolding(Folding<T> original, Predicate<T> predicate) {
        this.original = original;
        this.predicate = predicate;
    }

    @Override
    public <R> R fold(Accumulator<T, R> accumulator) {
        return original.fold(new TakingWhilePredicateSatisfiedAccumulator<>(accumulator, predicate));
    }
}
