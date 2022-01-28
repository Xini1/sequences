package org.example;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Maxim Tereshchenko
 */
public interface Sequence<T> extends Folding<T> {

    Sequence<T> filter(Predicate<T> predicate);

    <R> Sequence<R> map(Function<T, R> mapper);

    <R> Sequence<R> flatMap(Function<T, Sequence<R>> mapper);

    Sequence<T> distinct();

    Sequence<T> sorted(Comparator<T> comparator);

    Sequence<T> onEach(Consumer<T> consumer);

    Sequence<T> limit(long size);

    Sequence<T> skip(long count);

    void forEach(Consumer<T> consumer);

    Optional<T> min(Comparator<T> comparator);

    long count();

    Optional<T> findFirst();

    boolean anyMatch(Predicate<T> predicate);

    boolean allMatch(Predicate<T> predicate);

    boolean noneMatch(Predicate<T> predicate);
}
