package org.example.sequence.base;

import org.example.accumulator.base.Accumulator;
import org.example.folding.IterableFolding;
import org.example.sequence.FoldingSequence;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Maxim Tereshchenko
 */
public final class IterableSequence<T> implements Sequence<T> {

    private final Sequence<T> original;

    public IterableSequence(Iterable<T> iterable) {
        original = new FoldingSequence<>(new IterableFolding<>(iterable));
    }

    @Override
    public <R> R fold(Accumulator<T, R> accumulator) {
        return original.fold(accumulator);
    }

    @Override
    public Sequence<T> filter(Predicate<T> predicate) {
        return original.filter(predicate);
    }

    @Override
    public <R> Sequence<R> map(Function<T, R> mapper) {
        return original.map(mapper);
    }

    @Override
    public <R> Sequence<R> flatMap(Function<T, Sequence<R>> mapper) {
        return original.flatMap(mapper);
    }

    @Override
    public Sequence<T> distinct() {
        return original.distinct();
    }

    @Override
    public Sequence<T> sorted(Comparator<T> comparator) {
        return original.sorted(comparator);
    }

    @Override
    public Sequence<T> onEach(Consumer<T> consumer) {
        return original.onEach(consumer);
    }

    @Override
    public Sequence<T> limit(long size) {
        return original.limit(size);
    }

    @Override
    public Sequence<T> skip(long count) {
        return original.skip(count);
    }

    @Override
    public void forEach(Consumer<T> consumer) {
        original.forEach(consumer);
    }

    @Override
    public Optional<T> minimum(Comparator<T> comparator) {
        return original.minimum(comparator);
    }

    @Override
    public long count() {
        return original.count();
    }

    @Override
    public Optional<T> findFirst() {
        return original.findFirst();
    }

    @Override
    public boolean anyMatch(Predicate<T> predicate) {
        return original.anyMatch(predicate);
    }

    @Override
    public boolean allMatch(Predicate<T> predicate) {
        return original.allMatch(predicate);
    }

    @Override
    public boolean noneMatch(Predicate<T> predicate) {
        return original.noneMatch(predicate);
    }
}
