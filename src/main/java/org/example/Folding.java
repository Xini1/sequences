package org.example;

/**
 * @author Maxim Tereshchenko
 */
interface Folding<T> {

    <R> R fold(Accumulator<T, R> accumulator);
}
