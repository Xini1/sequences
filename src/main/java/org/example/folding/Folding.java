package org.example.folding;

import org.example.accumulator.base.Accumulator;

/**
 * @author Maxim Tereshchenko
 */
public interface Folding<T> {

    <R> R fold(Accumulator<T, R> accumulator);
}
