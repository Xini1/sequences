package org.example.accumulator;

import org.example.accumulator.base.Accumulator;

import java.util.function.Consumer;

/**
 * @author Maxim Tereshchenko
 */
public final class ConsumerAccumulator<T> implements Accumulator<T, Void> {

    private final Consumer<T> consumer;

    public ConsumerAccumulator(Consumer<T> consumer) {
        this.consumer = consumer;
    }

    @Override
    public boolean canAccept() {
        return true;
    }

    @Override
    public Accumulator<T, Void> onElement(T element) {
        consumer.accept(element);

        return this;
    }

    @Override
    public Void onFinish() {
        return null;
    }
}
