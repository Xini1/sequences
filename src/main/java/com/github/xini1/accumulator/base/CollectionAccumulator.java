package com.github.xini1.accumulator.base;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

/**
 * @author Maxim Tereshchenko
 */
public final class CollectionAccumulator<T> implements Accumulator<T, Collection<T>> {

    private final Supplier<Collection<T>> collectionSupplier;
    private final Collection<T> collection;

    private CollectionAccumulator(Supplier<Collection<T>> collectionSupplier, Collection<T> collection) {
        this.collectionSupplier = collectionSupplier;
        this.collection = Collections.unmodifiableCollection(collection);
    }

    public CollectionAccumulator(Supplier<Collection<T>> collectionSupplier) {
        this(collectionSupplier, collectionSupplier.get());
    }

    @Override
    public boolean canAccept() {
        return true;
    }

    @Override
    public Accumulator<T, Collection<T>> onElement(T element) {
        var copy = copy();
        copy.add(element);

        return new CollectionAccumulator<>(collectionSupplier, copy);
    }

    @Override
    public Collection<T> onFinish() {
        return copy();
    }

    private Collection<T> copy() {
        var copy = collectionSupplier.get();
        copy.addAll(collection);

        return copy;
    }
}
