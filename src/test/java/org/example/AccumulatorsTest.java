package org.example;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import org.example.accumulator.base.Accumulator;
import org.example.accumulator.base.CollectionAccumulator;
import org.example.accumulator.base.ListAccumulator;
import org.example.sequence.base.ArraySequence;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Maxim Tereshchenko
 */
final class AccumulatorsTest {

    private static List<Arguments> accumulators() {
        return List.of(
                arguments(new ListAccumulator<>(), List.of(1)),
                arguments(new CollectionAccumulator<>(ArrayList::new), List.of(1))
        );
    }

    @ParameterizedTest
    @MethodSource("accumulators")
    <T> void givenAccumulator_whenFold_thenResultAsExpected(Accumulator<Integer, T> accumulator, T expected) {
        assertThat(
                new ArraySequence<>(1)
                        .fold(new ListAccumulator<>())
        )
                .isEqualTo(expected);
    }
}
