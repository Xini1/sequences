package com.github.xini1.folding;

import com.github.xini1.accumulator.base.Accumulator;

/**
 * @author Maxim Tereshchenko
 */
public interface Folding<T> {

    <R> R fold(Accumulator<T, R> accumulator);
}
