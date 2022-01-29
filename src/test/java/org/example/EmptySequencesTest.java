package org.example;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import org.example.accumulator.base.ListAccumulator;
import org.example.sequence.base.ArraySequence;
import org.example.sequence.base.EmptySequence;
import org.example.sequence.base.OptionalSequence;
import org.example.sequence.base.Sequence;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Maxim Tereshchenko
 */
final class EmptySequencesTest {

    private static List<Arguments> sequences() {
        return List.of(
                arguments(new EmptySequence<>()),
                arguments(new OptionalSequence<>(Optional.empty()))
        );
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenEmptySequence_whenFold_thenEmptyList(Sequence<Integer> sequence) {
        assertThat(sequence.fold(new ListAccumulator<>())).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenEmptySequence_whenFilter_thenNoElements(Sequence<Integer> sequence) {
        assertThat(
                sequence.filter(num -> true)
                        .fold(new ListAccumulator<>())
        )
                .isEmpty();
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenEmptySequence_whenMap_thenNoElements(Sequence<Integer> sequence) {
        assertThat(
                sequence.map(num -> num + 1)
                        .fold(new ListAccumulator<>())
        )
                .isEmpty();
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenEmptySequence_whenFlatMap_thenNoElements(Sequence<Integer> sequence) {
        assertThat(
                sequence.flatMap(num -> new ArraySequence<>(num, num))
                        .fold(new ListAccumulator<>())
        )
                .isEmpty();
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenEmptySequence_whenDistinct_thenNoElements(Sequence<Integer> sequence) {
        assertThat(
                sequence.distinct()
                        .fold(new ListAccumulator<>())
        )
                .isEmpty();
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenEmptySequence_whenSorted_thenNoElements(Sequence<Integer> sequence) {
        assertThat(
                sequence.sorted(Comparator.naturalOrder())
                        .fold(new ListAccumulator<>())
        )
                .isEmpty();
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenEmptySequence_whenOnEach_thenConsumerIsNotInvoked(Sequence<Integer> sequence) {
        var counter = new AtomicInteger();

        sequence.onEach(unused -> counter.incrementAndGet())
                .fold(new ListAccumulator<>());

        assertThat(counter.get()).isZero();
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenEmptySequence_whenForEach_thenNoElements(Sequence<Integer> sequence) {
        var list = new ArrayList<Integer>();

        sequence.forEach(list::add);

        assertThat(list).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenEmptySequence_whenLimit_thenNoElements(Sequence<Integer> sequence) {
        assertThat(
                sequence.limit(2)
                        .fold(new ListAccumulator<>())
        )
                .isEmpty();
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenEmptySequence_whenSkip_thenNoElements(Sequence<Integer> sequence) {
        assertThat(
                sequence.skip(2)
                        .fold(new ListAccumulator<>())
        )
                .isEmpty();
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenEmptySequence_whenTakeWhile_thenNoElements(Sequence<Integer> sequence) {
        assertThat(
                sequence.takeWhile(num -> true)
                        .fold(new ListAccumulator<>())
        )
                .isEmpty();
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenEmptySequence_whenDropWhile_thenNoElements(Sequence<Integer> sequence) {
        assertThat(
                sequence.dropWhile(num -> false)
                        .fold(new ListAccumulator<>())
        )
                .isEmpty();
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenEmptySequence_whenMinimum_thenEmptyOptionalReturned(Sequence<Integer> sequence) {
        assertThat(sequence.minimum(Comparator.naturalOrder())).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenEmptySequence_whenCount_thenZeroReturned(Sequence<Integer> sequence) {
        assertThat(sequence.count()).isZero();
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenEmptySequence_whenFindFirst_thenEmptyOptionalReturned(Sequence<Integer> sequence) {
        assertThat(sequence.findFirst()).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenEmptySequence_whenAnyMatch_thenFalseReturned(Sequence<Integer> sequence) {
        assertThat(sequence.anyMatch(num -> true)).isFalse();
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenEmptySequence_whenAllMatch_thenTrueReturned(Sequence<Integer> sequence) {
        assertThat(sequence.allMatch(num -> true)).isTrue();
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenEmptySequence_whenNoneMatchEven_thenTrueReturned(Sequence<Integer> sequence) {
        assertThat(sequence.noneMatch(num -> true)).isTrue();
    }
}
