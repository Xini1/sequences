package org.example.sequence;

import org.example.accumulator.AllMatchingAccumulator;
import org.example.accumulator.ConsumerAccumulator;
import org.example.accumulator.CountingAccumulator;
import org.example.accumulator.InitialAnyMatchingAccumulator;
import org.example.accumulator.InitialFindingFirstAccumulator;
import org.example.accumulator.InitialSearchingForMinimumAccumulator;
import org.example.accumulator.NoneMatchingAccumulator;
import org.example.accumulator.base.Accumulator;
import org.example.folding.DistinctFolding;
import org.example.folding.FilteredFolding;
import org.example.folding.FlatMappedFolding;
import org.example.folding.Folding;
import org.example.folding.FoldingWithSkippedElements;
import org.example.folding.LimitedFolding;
import org.example.folding.MappedFolding;
import org.example.folding.SortedFolding;
import org.example.sequence.base.Sequence;
import org.example.shared.ConsumerFunctionAdapter;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Maxim Tereshchenko
 */
public final class FoldingSequence<T> implements Sequence<T> {

    private final Folding<T> folding;

    public FoldingSequence(Folding<T> folding) {
        this.folding = folding;
    }

    @Override
    public <R> R fold(Accumulator<T, R> accumulator) {
        return folding.fold(accumulator);
    }

    @Override
    public Sequence<T> filter(Predicate<T> predicate) {
        return new FoldingSequence<>(new FilteredFolding<>(folding, predicate));
    }

    @Override
    public <R> Sequence<R> map(Function<T, R> mapper) {
        return new FoldingSequence<>(new MappedFolding<>(folding, mapper));
    }

    @Override
    public <R> Sequence<R> flatMap(Function<T, Sequence<R>> mapper) {
        return new FoldingSequence<>(new FlatMappedFolding<>(new MappedFolding<>(folding, mapper)));
    }

    @Override
    public Sequence<T> distinct() {
        return new FoldingSequence<>(new DistinctFolding<>(folding));
    }

    @Override
    public Sequence<T> sorted(Comparator<T> comparator) {
        return new FoldingSequence<>(new SortedFolding<>(folding, comparator));
    }

    @Override
    public Sequence<T> onEach(Consumer<T> consumer) {
        return map(new ConsumerFunctionAdapter<>(consumer));
    }

    @Override
    public Sequence<T> limit(long size) {
        return new FoldingSequence<>(new LimitedFolding<>(folding, size));
    }

    @Override
    public Sequence<T> skip(long count) {
        return new FoldingSequence<>(new FoldingWithSkippedElements<>(folding, count));
    }

    @Override
    public void forEach(Consumer<T> consumer) {
        folding.fold(new ConsumerAccumulator<>(consumer));
    }

    @Override
    public Optional<T> minimum(Comparator<T> comparator) {
        return folding.fold(new InitialSearchingForMinimumAccumulator<>(comparator));
    }

    @Override
    public long count() {
        return folding.fold(new CountingAccumulator<>());
    }

    @Override
    public Optional<T> findFirst() {
        return folding.fold(new InitialFindingFirstAccumulator<>());
    }

    @Override
    public boolean anyMatch(Predicate<T> predicate) {
        return folding.fold(new InitialAnyMatchingAccumulator<>(predicate));
    }

    @Override
    public boolean allMatch(Predicate<T> predicate) {
        return folding.fold(new AllMatchingAccumulator<>(predicate));
    }

    @Override
    public boolean noneMatch(Predicate<T> predicate) {
        return folding.fold(new NoneMatchingAccumulator<>(predicate));
    }
}
