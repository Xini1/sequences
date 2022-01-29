package org.example.sequence.base;

import org.example.accumulator.base.Accumulator;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Maxim Tereshchenko
 */
public final class SingleElementSequence<T> implements Sequence<T> {

    private final T element;

    public SingleElementSequence(T element) {
        this.element = element;
    }

    @Override
    public <R> R fold(Accumulator<T, R> accumulator) {
        return accumulator.onElement(element)
                .onFinish();
    }

    @Override
    public Sequence<T> filter(Predicate<T> predicate) {
        if (predicate.test(element)) {
            return this;
        }

        return new EmptySequence<>();
    }

    @Override
    public <R> Sequence<R> map(Function<T, R> mapper) {
        return new SingleElementSequence<>(mapper.apply(element));
    }

    @Override
    public <R> Sequence<R> flatMap(Function<T, Sequence<R>> mapper) {
        return mapper.apply(element);
    }

    @Override
    public Sequence<T> distinct() {
        return this;
    }

    @Override
    public Sequence<T> sorted(Comparator<T> comparator) {
        return this;
    }

    @Override
    public Sequence<T> onEach(Consumer<T> consumer) {
        consumer.accept(element);

        return this;
    }

    @Override
    public Sequence<T> limit(long size) {
        if (size == 0) {
            return new EmptySequence<>();
        }

        return this;
    }

    @Override
    public Sequence<T> skip(long count) {
        if (count == 0) {
            return this;
        }

        return new EmptySequence<>();
    }

    @Override
    public Sequence<T> takeWhile(Predicate<T> predicate) {
        return filter(predicate);
    }

    @Override
    public void forEach(Consumer<T> consumer) {
        consumer.accept(element);
    }

    @Override
    public Optional<T> minimum(Comparator<T> comparator) {
        return Optional.of(element);
    }

    @Override
    public long count() {
        return 1;
    }

    @Override
    public Optional<T> findFirst() {
        return Optional.of(element);
    }

    @Override
    public boolean anyMatch(Predicate<T> predicate) {
        return predicate.test(element);
    }

    @Override
    public boolean allMatch(Predicate<T> predicate) {
        return predicate.test(element);
    }

    @Override
    public boolean noneMatch(Predicate<T> predicate) {
        return !predicate.test(element);
    }
}
