package com.github.xini1.folding;

import com.github.xini1.accumulator.MappingAccumulator;
import com.github.xini1.accumulator.base.Accumulator;

import java.util.function.Function;

/**
 * @author Maxim Tereshchenko
 */
public final class MappedFolding<T, S> implements Folding<S> {

    private final Folding<T> original;
    private final Function<T, S> mapper;

    public MappedFolding(Folding<T> original, Function<T, S> mapper) {
        this.original = original;
        this.mapper = mapper;
    }

    @Override
    public <R> R fold(Accumulator<S, R> accumulator) {
        return original.fold(new MappingAccumulator<>(accumulator, mapper));
    }
}
