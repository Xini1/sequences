package org.example;

/**
 * @author Maxim Tereshchenko
 */
class IterableFolding<T> implements Folding<T> {

    private final Iterable<T> iterable;

    IterableFolding(Iterable<T> iterable) {
        this.iterable = iterable;
    }

    @Override
    public <R> R fold(Accumulator<T, R> accumulator) {
        var current = accumulator;

        for (T element : iterable) {
            current = current.onElement(element);
        }

        return current.onFinish();
    }
}
