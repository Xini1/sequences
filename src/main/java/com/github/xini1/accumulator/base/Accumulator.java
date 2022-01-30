package com.github.xini1.accumulator.base;

/**
 * @author Maxim Tereshchenko
 */
public interface Accumulator<T, R> {

    boolean canAccept();

    Accumulator<T, R> onElement(T element);

    R onFinish();
}
