package org.example.folding;

import org.example.accumulator.SortingAccumulator;
import org.example.accumulator.base.Accumulator;

import java.util.Comparator;

/**
 * @author Maxim Tereshchenko
 */
public final class SortedFolding<T> implements Folding<T> {

    private final Folding<T> folding;
    private final Comparator<T> comparator;

    public SortedFolding(Folding<T> folding, Comparator<T> comparator) {
        this.folding = folding;
        this.comparator = comparator;
    }

    @Override
    public <R> R fold(Accumulator<T, R> accumulator) {
        return folding.fold(new SortingAccumulator<>(accumulator, comparator));
    }
}
