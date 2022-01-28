package org.example;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Maxim Tereshchenko
 */
class FoldingSequence<T> implements Sequence<T> {

    private final Folding<T> folding;

    FoldingSequence(Folding<T> folding) {
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
    public Optional<T> min(Comparator<T> comparator) {
        return folding.fold(new EmptySearchingForMinimumAccumulator<>(comparator));
    }

    @Override
    public long count() {
        return folding.fold(new CountingAccumulator<>());
    }

    @Override
    public Optional<T> findFirst() {
        return folding.fold(new EmptyFindingFirstAccumulator<>());
    }

    @Override
    public boolean anyMatch(Predicate<T> predicate) {
        return folding.fold(new MatchingAnyAccumulator<>(predicate));
    }

    @Override
    public boolean allMatch(Predicate<T> predicate) {
        return folding.fold(new MatchingAllAccumulator<>(predicate));
    }

    @Override
    public boolean noneMatch(Predicate<T> predicate) {
        return folding.fold(new NoneMatchingAccumulator<>(predicate));
    }

    private static final class ConsumerAccumulator<T> implements Accumulator<T, Void> {

        private final Consumer<T> consumer;

        private ConsumerAccumulator(Consumer<T> consumer) {
            this.consumer = consumer;
        }

        @Override
        public Accumulator<T, Void> onElement(T element) {
            consumer.accept(element);

            return new ConsumerAccumulator<>(consumer);
        }

        @Override
        public Void onFinish() {
            return null;
        }
    }

    private static final class EmptySearchingForMinimumAccumulator<T> implements Accumulator<T, Optional<T>> {

        private final Comparator<T> comparator;

        private EmptySearchingForMinimumAccumulator(Comparator<T> comparator) {
            this.comparator = comparator;
        }

        @Override
        public Accumulator<T, Optional<T>> onElement(T element) {
            return new SearchingForMinimumAccumulator<>(comparator, element);
        }

        @Override
        public Optional<T> onFinish() {
            return Optional.empty();
        }
    }

    private static final class SearchingForMinimumAccumulator<T> implements Accumulator<T, Optional<T>> {

        private final Comparator<T> comparator;
        private final T currentMinimum;

        private SearchingForMinimumAccumulator(Comparator<T> comparator, T currentMinimum) {
            this.comparator = comparator;
            this.currentMinimum = currentMinimum;
        }

        @Override
        public Accumulator<T, Optional<T>> onElement(T element) {
            return new SearchingForMinimumAccumulator<>(comparator, minimum(element));
        }

        @Override
        public Optional<T> onFinish() {
            return Optional.of(currentMinimum);
        }

        private T minimum(T element) {
            if (comparator.compare(element, currentMinimum) < 0) {
                return element;
            }

            return currentMinimum;
        }
    }

    private static final class CountingAccumulator<T> implements Accumulator<T, Long> {

        private final long runningTotal;

        private CountingAccumulator(long runningTotal) {
            this.runningTotal = runningTotal;
        }

        private CountingAccumulator() {
            this(0);
        }

        @Override
        public Accumulator<T, Long> onElement(T element) {
            return new CountingAccumulator<>(runningTotal + 1);
        }

        @Override
        public Long onFinish() {
            return runningTotal;
        }
    }

    private static final class EmptyFindingFirstAccumulator<T> implements Accumulator<T, Optional<T>> {

        @Override
        public Accumulator<T, Optional<T>> onElement(T element) {
            return new FindingFirstAccumulator<>(element);
        }

        @Override
        public Optional<T> onFinish() {
            return Optional.empty();
        }
    }

    private static final class FindingFirstAccumulator<T> implements Accumulator<T, Optional<T>> {

        private final T first;

        private FindingFirstAccumulator(T first) {
            this.first = first;
        }

        @Override
        public Accumulator<T, Optional<T>> onElement(T element) {
            return new FindingFirstAccumulator<>(first);
        }

        @Override
        public Optional<T> onFinish() {
            return Optional.of(first);
        }
    }

    private static final class MatchingAnyAccumulator<T> implements Accumulator<T, Boolean> {

        private final Predicate<T> predicate;

        private MatchingAnyAccumulator(Predicate<T> predicate) {
            this.predicate = predicate;
        }

        @Override
        public Accumulator<T, Boolean> onElement(T element) {
            if (predicate.test(element)) {
                return new MatchedAnyAccumulator<>();
            }

            return new MatchingAnyAccumulator<>(predicate);
        }

        @Override
        public Boolean onFinish() {
            return Boolean.FALSE;
        }
    }

    private static final class MatchedAnyAccumulator<T> implements Accumulator<T, Boolean> {

        @Override
        public Accumulator<T, Boolean> onElement(T element) {
            return new MatchedAnyAccumulator<>();
        }

        @Override
        public Boolean onFinish() {
            return Boolean.TRUE;
        }
    }

    private static final class MatchingAllAccumulator<T> implements Accumulator<T, Boolean> {

        private final Predicate<T> predicate;

        private MatchingAllAccumulator(Predicate<T> predicate) {
            this.predicate = predicate;
        }

        @Override
        public Accumulator<T, Boolean> onElement(T element) {
            if (predicate.test(element)) {
                return new MatchingAllAccumulator<>(predicate);
            }

            return new NotAllMatchedAccumulator<>();
        }

        @Override
        public Boolean onFinish() {
            return Boolean.TRUE;
        }
    }

    private static final class NotAllMatchedAccumulator<T> implements Accumulator<T, Boolean> {

        @Override
        public Accumulator<T, Boolean> onElement(T element) {
            return new NotAllMatchedAccumulator<>();
        }

        @Override
        public Boolean onFinish() {
            return Boolean.FALSE;
        }
    }

    private static final class NoneMatchingAccumulator<T> implements Accumulator<T, Boolean> {

        private final Predicate<T> predicate;

        private NoneMatchingAccumulator(Predicate<T> predicate) {
            this.predicate = predicate;
        }

        @Override
        public Accumulator<T, Boolean> onElement(T element) {
            if (predicate.test(element)) {
                return new MatchedOneAccumulator<>();
            }

            return new NoneMatchingAccumulator<>(predicate);
        }

        @Override
        public Boolean onFinish() {
            return Boolean.TRUE;
        }
    }

    private static final class MatchedOneAccumulator<T> implements Accumulator<T, Boolean> {

        @Override
        public Accumulator<T, Boolean> onElement(T element) {
            return new MatchedOneAccumulator<>();
        }

        @Override
        public Boolean onFinish() {
            return Boolean.FALSE;
        }
    }
}
