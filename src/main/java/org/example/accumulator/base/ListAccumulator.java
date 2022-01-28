package org.example.accumulator.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Maxim Tereshchenko
 */
public final class ListAccumulator<T> implements Accumulator<T, List<T>> {

    private final List<T> list;

    private ListAccumulator(List<T> list) {
        this.list = Collections.unmodifiableList(list);
    }

    public ListAccumulator() {
        this(new ArrayList<>());
    }

    @Override
    public Accumulator<T, List<T>> onElement(T element) {
        var copy = new ArrayList<>(list);
        copy.add(element);

        return new ListAccumulator<>(copy);
    }

    @Override
    public List<T> onFinish() {
        return new ArrayList<>(list);
    }
}
