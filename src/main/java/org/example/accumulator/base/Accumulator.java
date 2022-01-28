package org.example.accumulator.base;

/**
 * @author Maxim Tereshchenko
 */
public interface Accumulator<T, R> {

    Accumulator<T, R> onElement(T element);

    R onFinish();
}
