package org.example;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import org.example.accumulator.base.Accumulator;
import org.example.accumulator.base.CollectionAccumulator;
import org.example.accumulator.base.JoiningAccumulator;
import org.example.accumulator.base.ListAccumulator;
import org.example.accumulator.base.SetAccumulator;
import org.example.sequence.base.ArraySequence;
import org.example.sequence.base.EmptySequence;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Maxim Tereshchenko
 */
final class AccumulatorsTest {

    private static List<Arguments> accumulators() {
        return List.of(
                arguments(new CollectionAccumulator<>(ArrayList::new), List.of("1", "1", "2")),
                arguments(new ListAccumulator<>(), List.of("1", "1", "2")),
                arguments(new SetAccumulator<>(), Set.of("1", "2")),
                arguments(new JoiningAccumulator<>("{", ", ", "}"), "{1, 1, 2}")
        );
    }

    @ParameterizedTest
    @MethodSource("accumulators")
    <T> void givenAccumulator_whenFold_thenResultAsExpected(Accumulator<String, T> accumulator, T expected) {
        assertThat(
                new ArraySequence<>("1", "1", "2")
                        .fold(accumulator)
        )
                .isEqualTo(expected);
    }

    @Test
    void givenEmptySequence_whenFoldWithJoiningAccumulator_thenValueIfNoElementsReturned() {
        assertThat(
                new EmptySequence<String>()
                        .fold(new JoiningAccumulator<>("empty", "", "", ""))
        )
                .isEqualTo("empty");
    }
}
