package com.github.xini1.sequence.base;

import com.github.xini1.accumulator.base.Accumulator;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Maxim Tereshchenko
 */
public final class EmptySequence<T> implements Sequence<T> {

    @Override
    public <R> R fold(Accumulator<T, R> accumulator) {
        return accumulator.onFinish();
    }

    @Override
    public Sequence<T> filter(Predicate<T> predicate) {
        return this;
    }

    @Override
    public <R> Sequence<R> map(Function<T, R> mapper) {
        return new EmptySequence<>();
    }

    @Override
    public <R> Sequence<R> flatMap(Function<T, Sequence<R>> mapper) {
        return new EmptySequence<>();
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
        return this;
    }

    @Override
    public Sequence<T> limit(long size) {
        return this;
    }

    @Override
    public Sequence<T> skip(long count) {
        return this;
    }

    @Override
    public Sequence<T> takeWhile(Predicate<T> predicate) {
        return this;
    }

    @Override
    public Sequence<T> dropWhile(Predicate<T> predicate) {
        return this;
    }

    @Override
    public void forEach(Consumer<T> consumer) {
        // empty
    }

    @Override
    public Optional<T> minimum(Comparator<T> comparator) {
        return Optional.empty();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public Optional<T> findFirst() {
        return Optional.empty();
    }

    @Override
    public boolean anyMatch(Predicate<T> predicate) {
        return false;
    }

    @Override
    public boolean allMatch(Predicate<T> predicate) {
        return true;
    }

    @Override
    public boolean noneMatch(Predicate<T> predicate) {
        return true;
    }
}
