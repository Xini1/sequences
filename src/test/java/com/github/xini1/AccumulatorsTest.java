package com.github.xini1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.github.xini1.accumulator.base.Accumulator;
import com.github.xini1.accumulator.base.CollectionAccumulator;
import com.github.xini1.accumulator.base.CombiningAccumulator;
import com.github.xini1.accumulator.base.GroupingAccumulator;
import com.github.xini1.accumulator.base.JoiningAccumulator;
import com.github.xini1.accumulator.base.ListAccumulator;
import com.github.xini1.accumulator.base.MapAccumulator;
import com.github.xini1.accumulator.base.PartitioningAccumulator;
import com.github.xini1.accumulator.base.ReducingAccumulator;
import com.github.xini1.accumulator.base.SetAccumulator;
import com.github.xini1.sequence.base.ArraySequence;
import com.github.xini1.sequence.base.EmptySequence;
import com.github.xini1.sequence.base.SingleElementSequence;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * @author Maxim Tereshchenko
 */
final class AccumulatorsTest {

    private static List<Arguments> accumulators() {
        return List.of(
                arguments(new CollectionAccumulator<>(ArrayList::new), List.of("1", "1", "2")),
                arguments(new ListAccumulator<>(), List.of("1", "1", "2")),
                arguments(new SetAccumulator<>(), Set.of("1", "2")),
                arguments(new JoiningAccumulator<>("{", ", ", "}"), "{1, 1, 2}"),
                arguments(new JoiningAccumulator<>(", "), "1, 1, 2"),
                arguments(new JoiningAccumulator<>(), "112"),
                arguments(new ReducingAccumulator<>("", (identity, element) -> identity + element), "112"),
                arguments(new CombiningAccumulator<String>((first, second) -> first + second), Optional.of("112")),
                arguments(
                        new GroupingAccumulator<String, Integer>(Integer::valueOf),
                        Map.of(1, List.of("1", "1"), 2, List.of("2"))
                ),
                arguments(
                        new PartitioningAccumulator<String>(str -> Integer.parseInt(str) == 1),
                        Map.of(true, List.of("1", "1"), false, List.of("2"))
                ),
                arguments(
                        new MapAccumulator<String, Integer, String>(
                                Integer::valueOf,
                                Function.identity(),
                                (existing, added) -> existing + added
                        ),
                        Map.of(1, "11", 2, "2")
                )
        );
    }

    private static List<Arguments> accumulatorsForEmptySequence() {
        return List.of(
                arguments(new JoiningAccumulator<>("empty", "", "", ""), "empty"),
                arguments(new CombiningAccumulator<String>((first, second) -> first + second), Optional.empty())
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

    @ParameterizedTest
    @MethodSource("accumulatorsForEmptySequence")
    <T> void givenEmptySequence_whenFold_thenResultAsExpected(
            Accumulator<String, T> accumulator,
            T expected
    ) {
        assertThat(
                new EmptySequence<String>()
                        .fold(accumulator)
        )
                .isEqualTo(expected);
    }

    @Test
    void givenSingleElementSequence_whenFoldWithCombiningAccumulator_thenElementReturned() {
        assertThat(
                new SingleElementSequence<>(1)
                        .fold(new CombiningAccumulator<>(Integer::sum))
        )
                .contains(1);
    }

    @Test
    void givenDuplicateKeysAfterMapping_whenFoldWithMapAccumulator_thenIllegalArgumentException() {
        var sequence = new ArraySequence<>(1, 1);
        var accumulator = new MapAccumulator<Integer, Integer, Integer>(Function.identity(), Function.identity());

        assertThatThrownBy(() -> sequence.fold(accumulator)).isInstanceOf(IllegalArgumentException.class);
    }
}
