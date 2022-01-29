package org.example;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import org.example.accumulator.base.ListAccumulator;
import org.example.sequence.base.IterableSequence;
import org.example.sequence.base.OptionalSequence;
import org.example.sequence.base.Sequence;
import org.example.sequence.base.SingleElementSequence;
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
final class SingleElementSequencesTest {

    private static List<Arguments> sequences() {
        return List.of(
                arguments(new SingleElementSequence<>(1)),
                arguments(new OptionalSequence<>(Optional.of(1)))
        );
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenNumberMatchingPredicate_whenFilter_thenNumberRemained(Sequence<Integer> sequence) {
        assertThat(
                sequence.filter(num -> true)
                        .fold(new ListAccumulator<>())
        )
                .containsExactly(1);
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenNumber_whenMap_thenNumberIncremented(Sequence<Integer> sequence) {
        assertThat(
                sequence.map(num -> num + 1)
                        .fold(new ListAccumulator<>())
        )
                .containsExactly(2);
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenNumber_whenFlatMap_thenTwoNumbers(Sequence<Integer> sequence) {
        assertThat(
                sequence.flatMap(num -> new IterableSequence<>(List.of(num, num + 1)))
                        .fold(new ListAccumulator<>())
        )
                .containsExactly(1, 2);
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenNumber_whenDistinct_thenNumberRemained(Sequence<Integer> sequence) {
        assertThat(
                sequence.distinct()
                        .fold(new ListAccumulator<>())
        )
                .containsExactly(1);
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenNumber_whenSorted_thenNumberRemained(Sequence<Integer> sequence) {
        assertThat(
                sequence.sorted(Comparator.naturalOrder())
                        .fold(new ListAccumulator<>())
        )
                .containsExactly(1);
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenNumber_whenSorted_thenCounterIncrementedOnce(Sequence<Integer> sequence) {
        var counter = new AtomicInteger();

        sequence.onEach(unused -> counter.incrementAndGet())
                .fold(new ListAccumulator<>());

        assertThat(counter.get()).isOne();
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenNumber_whenForEach_thenNumberAddedToList(Sequence<Integer> sequence) {
        var list = new ArrayList<Integer>();

        sequence.forEach(list::add);

        assertThat(list).containsExactly(1);
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenNumber_whenLimit1_thenNumberRemained(Sequence<Integer> sequence) {
        assertThat(
                sequence.limit(1)
                        .fold(new ListAccumulator<>())
        )
                .containsExactly(1);
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenNumber_whenSkip1_thenNumberSkipped(Sequence<Integer> sequence) {
        assertThat(
                sequence.skip(1)
                        .fold(new ListAccumulator<>())
        )
                .isEmpty();
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenNumber_whenTakeWhile_thenNumberRemained(Sequence<Integer> sequence) {
        assertThat(
                sequence.takeWhile(num -> true)
                        .fold(new ListAccumulator<>())
        )
                .containsExactly(1);
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenNumber_whenDropWhile_thenNumberRemained(Sequence<Integer> sequence) {
        assertThat(
                sequence.dropWhile(num -> false)
                        .fold(new ListAccumulator<>())
        )
                .containsExactly(1);
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenNumber_whenMinimum_thenNumberRemained(Sequence<Integer> sequence) {
        assertThat(sequence.minimum(Comparator.naturalOrder())).contains(1);
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenNumber_whenCount_thenOneReturned(Sequence<Integer> sequence) {
        assertThat(sequence.count()).isOne();
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenNumber_whenFindFirst_thenNumberReturned(Sequence<Integer> sequence) {
        assertThat(sequence.findFirst()).contains(1);
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenNumber_whenAnyMatch_thenTrueReturned(Sequence<Integer> sequence) {
        assertThat(sequence.anyMatch(num -> true)).isTrue();
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenNumber_whenAllMatch_thenTrueReturned(Sequence<Integer> sequence) {
        assertThat(sequence.allMatch(num -> true)).isTrue();
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void givenNumber_whenNoneMatch_thenTrueReturned(Sequence<Integer> sequence) {
        assertThat(sequence.noneMatch(num -> false)).isTrue();
    }
}
