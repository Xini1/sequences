package org.example.shared;

import java.util.function.BinaryOperator;

/**
 * @author Maxim Tereshchenko
 */
public class ThrowingBinaryOperator<T> implements BinaryOperator<T> {

    @Override
    public T apply(T element1, T element2) {
        throw new IllegalArgumentException(
                String.format(
                        "Duplicate key encountered: attempted merging values %s and %s",
                        element1,
                        element2
                )
        );
    }
}
