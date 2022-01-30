package org.example.accumulator;

import org.example.accumulator.base.Accumulator;

import java.util.function.Function;

/**
 * @author Maxim Tereshchenko
 */
public final class MappingAccumulator<T, S, R> implements Accumulator<T, R> {

    private final Accumulator<S, R> original;
    private final Function<T, S> mapper;

    public MappingAccumulator(Accumulator<S, R> original, Function<T, S> mapper) {
        this.original = original;
        this.mapper = mapper;
    }

    @Override
    public boolean canAccept() {
        return original.canAccept();
    }

    @Override
    public Accumulator<T, R> onElement(T element) {
        return new MappingAccumulator<>(original.onElement(mapper.apply(element)), mapper);
    }

    @Override
    public R onFinish() {
        return original.onFinish();
    }
}
