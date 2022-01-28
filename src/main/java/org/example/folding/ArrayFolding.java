package org.example.folding;

import org.example.accumulator.base.Accumulator;

/**
 * @author Maxim Tereshchenko
 */
public final class ArrayFolding<T> implements Folding<T> {

    private final T[] array;

    public ArrayFolding(T[] array) {
        this.array = array.clone();
    }

    @Override
    public <R> R fold(Accumulator<T, R> accumulator) {
        var current = accumulator;

        for (T element : array) {
            current = current.onElement(element);
        }

        return accumulator.onFinish();
    }
}
