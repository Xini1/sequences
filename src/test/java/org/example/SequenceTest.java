package org.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.example.accumulator.base.ListAccumulator;
import org.example.sequence.base.IterableSequence;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Maxim Tereshchenko
 */
class SequenceTest {

    @Test
    void givenNumbers_whenFold_thenSameNumbers() {
        assertThat(
                new IterableSequence<>(List.of(1, 2))
                        .fold(new ListAccumulator<>())
        )
                .containsExactly(1, 2);
    }

    @Test
    void givenEvenAndOddNumbers_whenFilter_thenOnlyEvens() {
        assertThat(
                new IterableSequence<>(List.of(1, 2))
                        .filter(num -> num % 2 == 0)
                        .fold(new ListAccumulator<>())
        )
                .containsExactly(2);
    }

    @Test
    void givenNumbers_whenMap_thenNumbersIncremented() {
        assertThat(
                new IterableSequence<>(List.of(1, 2))
                        .map(num -> num + 1)
                        .fold(new ListAccumulator<>())
        )
                .containsExactly(2, 3);
    }

    @Test
    void givenTwoNumbers_whenFlatMap_thenFourNumbers() {
        assertThat(
                new IterableSequence<>(List.of(1, 2))
                        .flatMap(num -> new IterableSequence<>(List.of(num, num + 1)))
                        .fold(new ListAccumulator<>())
        )
                .containsExactly(1, 2, 2, 3);
    }

    @Test
    void givenNumbersWithDuplicates_whenDistinct_thenUniqueNumbers() {
        assertThat(
                new IterableSequence<>(List.of(1, 1))
                        .distinct()
                        .fold(new ListAccumulator<>())
        )
                .containsExactly(1);
    }

    @Test
    void givenUnsortedNumbers_whenSorted_thenNumbersSorted() {
        assertThat(
                new IterableSequence<>(List.of(2, 1))
                        .sorted(Comparator.naturalOrder())
                        .fold(new ListAccumulator<>())
        )
                .containsExactly(1, 2);
    }

    @Test
    void givenNumbers_whenOnEach_thenCounterIncremented() {
        var counter = new AtomicInteger();

        new IterableSequence<>(List.of(1, 2))
                .onEach(unused -> counter.incrementAndGet())
                .fold(new ListAccumulator<>());

        assertThat(counter.get()).isEqualTo(2);
    }

    @Test
    void givenNumbers_whenOnEach_thenNumbersAddedToList() {
        var list = new ArrayList<Integer>();

        new IterableSequence<>(List.of(1, 2))
                .forEach(list::add);

        assertThat(list).containsExactly(1, 2);
    }

    @Test
    void givenManyNumbers_whenLimit_thenThereAreLessThenLimitSizeElements() {
        assertThat(
                new IterableSequence<>(List.of(1, 2, 3))
                        .limit(2)
                        .fold(new ListAccumulator<>())
        )
                .containsExactly(1, 2);
    }

    @Test
    void givenManyNumbers_whenSkip_thenFirstElementsSkipped() {
        assertThat(
                new IterableSequence<>(List.of(1, 2, 3))
                        .skip(1)
                        .fold(new ListAccumulator<>())
        )
                .containsExactly(2, 3);
    }

    @Test
    void givenFlatMappedSequence_whenSkip_thenSkipOnlyFirstElementsOfFlatMappedSequence() {
        assertThat(
                new IterableSequence<>(List.of(1, 2))
                        .flatMap(num -> new IterableSequence<>(List.of(num, num)))
                        .skip(1)
                        .fold(new ListAccumulator<>())
        )
                .containsExactly(1, 2, 2);
    }

    @Test
    void givenFlatMappedSequence_whenLimit_thenThereAreLessThenLimitSizeElements() {
        assertThat(
                new IterableSequence<>(List.of(1, 2))
                        .flatMap(num -> new IterableSequence<>(List.of(num, num)))
                        .limit(3)
                        .fold(new ListAccumulator<>())
        )
                .containsExactly(1, 1, 2);
    }

    @Test
    void givenFlatMappedSequenceWithDuplicates_whenDistinct_thenUniqueNumbers() {
        assertThat(
                new IterableSequence<>(List.of(1, 2))
                        .flatMap(num -> new IterableSequence<>(List.of(num, num + 1)))
                        .distinct()
                        .fold(new ListAccumulator<>())
        )
                .containsExactly(1, 2, 3);
    }

    @Test
    void givenUnsortedFlatMappedSequence_whenSorted_thenNumbersSorted() {
        assertThat(
                new IterableSequence<>(List.of(2, 1))
                        .flatMap(num -> new IterableSequence<>(List.of(num, num + 1)))
                        .sorted(Comparator.naturalOrder())
                        .fold(new ListAccumulator<>())
        )
                .containsExactly(1, 2, 2, 3);
    }

    @Test
    void givenNumbers_whenMin_thenMinimumNumberIsFound() {
        assertThat(
                new IterableSequence<>(List.of(1, 2))
                        .min(Comparator.naturalOrder())
        )
                .contains(1);
    }

    @Test
    void givenFlatMappedNumbers_whenMin_thenMinimumNumberIsFound() {
        assertThat(
                new IterableSequence<>(List.of(1, 2))
                        .flatMap(num -> new IterableSequence<>(List.of(-num, num + 1)))
                        .min(Comparator.naturalOrder())
        )
                .contains(-2);
    }

    @Test
    void givenEmptySequence_whenMin_thenEmptyOptionalReturned() {
        assertThat(
                new IterableSequence<>(List.of(1, 2))
                        .skip(2)
                        .min(Comparator.naturalOrder())
        )
                .isEmpty();
    }

    @Test
    void givenNumbers_whenCount_thenNumbersCounted() {
        assertThat(
                new IterableSequence<>(List.of(1, 2))
                        .count()
        )
                .isEqualTo(2);
    }

    @Test
    void givenFlatMappedNumbers_whenCount_thenNumbersCounted() {
        assertThat(
                new IterableSequence<>(List.of(1, 2))
                        .flatMap(num -> new IterableSequence<>(List.of(num, num)))
                        .count()
        )
                .isEqualTo(4);
    }

    @Test
    void givenNumbers_whenFindFirst_thenFirstNumberIsFound() {
        assertThat(
                new IterableSequence<>(List.of(1, 2))
                        .findFirst()
        )
                .contains(1);
    }

    @Test
    void givenOddNumbers_whenAnyMatchEven_thenFalseReturned() {
        assertThat(
                new IterableSequence<>(List.of(1, 3))
                        .anyMatch(num -> num % 2 == 0)
        )
                .isFalse();
    }

    @Test
    void givenNumbersWithEven_whenAnyMatchEven_thenTrueReturned() {
        assertThat(
                new IterableSequence<>(List.of(1, 2))
                        .anyMatch(num -> num % 2 == 0)
        )
                .isTrue();
    }

    @Test
    void givenEmptySequence_whenAnyMatchEven_thenFalseReturned() {
        assertThat(
                new IterableSequence<>(List.of(1, 2))
                        .skip(2)
                        .anyMatch(num -> num % 2 == 0)
        )
                .isFalse();
    }

    @Test
    void givenNumbersWithOdd_whenAllMatchEven_thenFalseReturned() {
        assertThat(
                new IterableSequence<>(List.of(1, 2))
                        .allMatch(num -> num % 2 == 0)
        )
                .isFalse();
    }

    @Test
    void givenEvenNumbers_whenAllMatchEven_thenTrueReturned() {
        assertThat(
                new IterableSequence<>(List.of(2, 4))
                        .allMatch(num -> num % 2 == 0)
        )
                .isTrue();
    }

    @Test
    void givenEmptySequence_whenAllMatchEven_thenTrueReturned() {
        assertThat(
                new IterableSequence<>(List.of(1, 2))
                        .skip(2)
                        .allMatch(num -> num % 2 == 0)
        )
                .isTrue();
    }

    @Test
    void givenNumbersWithEven_whenNoneMatchEven_thenFalseReturned() {
        assertThat(
                new IterableSequence<>(List.of(1, 2))
                        .noneMatch(num -> num % 2 == 0)
        )
                .isFalse();
    }

    @Test
    void givenOddNumbers_whenNoneMatchEven_thenTrueReturned() {
        assertThat(
                new IterableSequence<>(List.of(1, 3))
                        .noneMatch(num -> num % 2 == 0)
        )
                .isTrue();
    }

    @Test
    void givenEmptySequence_whenNoneMatchEven_thenTrueReturned() {
        assertThat(
                new IterableSequence<>(List.of(1, 2))
                        .skip(2)
                        .noneMatch(num -> num % 2 == 0)
        )
                .isTrue();
    }
}