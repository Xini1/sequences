package org.example;

import java.util.function.Function;

/**
 * @author Maxim Tereshchenko
 */
class MappedFolding<T, S> implements Folding<S> {

    private final Folding<T> original;
    private final Function<T, S> mapper;

    MappedFolding(Folding<T> original, Function<T, S> mapper) {
        this.original = original;
        this.mapper = mapper;
    }

    @Override
    public <R> R fold(Accumulator<S, R> accumulator) {
        return original.fold(new MappingAccumulator<>(accumulator, mapper));
    }

    private static final class MappingAccumulator<T, S, R> implements Accumulator<T, R> {

        private final Accumulator<S, R> original;
        private final Function<T, S> mapper;

        private MappingAccumulator(Accumulator<S, R> original, Function<T, S> mapper) {
            this.original = original;
            this.mapper = mapper;
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
}
