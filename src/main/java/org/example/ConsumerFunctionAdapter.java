package org.example;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * @author Maxim Tereshchenko
 */
class ConsumerFunctionAdapter<T> implements UnaryOperator<T> {

    private final Consumer<T> consumer;

    ConsumerFunctionAdapter(Consumer<T> consumer) {
        this.consumer = consumer;
    }

    @Override
    public T apply(T element) {
        consumer.accept(element);

        return element;
    }
}
