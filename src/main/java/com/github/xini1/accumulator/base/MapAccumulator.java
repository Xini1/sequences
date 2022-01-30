package com.github.xini1.accumulator.base;

import com.github.xini1.shared.ThrowingBinaryOperator;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * @author Maxim Tereshchenko
 */
public final class MapAccumulator<T, K, V> implements Accumulator<T, Map<K, V>> {

    private final Function<T, K> keyMapper;
    private final Function<T, V> valueMapper;
    private final BinaryOperator<V> valueMerger;
    private final Map<K, V> map;

    private MapAccumulator(
            Function<T, K> keyMapper,
            Function<T, V> valueMapper,
            BinaryOperator<V> valueMerger,
            Map<K, V> map
    ) {
        this.keyMapper = keyMapper;
        this.valueMapper = valueMapper;
        this.valueMerger = valueMerger;
        this.map = map;
    }

    public MapAccumulator(Function<T, K> keyMapper, Function<T, V> valueMapper, BinaryOperator<V> valueMerger) {
        this(keyMapper, valueMapper, valueMerger, new HashMap<>());
    }

    public MapAccumulator(Function<T, K> keyMapper, Function<T, V> valueMapper) {
        this(keyMapper, valueMapper, new ThrowingBinaryOperator<>());
    }

    @Override
    public boolean canAccept() {
        return true;
    }

    @Override
    public Accumulator<T, Map<K, V>> onElement(T element) {
        var copy = new HashMap<>(map);
        copy.merge(keyMapper.apply(element), valueMapper.apply(element), valueMerger);

        return new MapAccumulator<>(keyMapper, valueMapper, valueMerger, copy);
    }

    @Override
    public Map<K, V> onFinish() {
        return Map.copyOf(map);
    }
}
