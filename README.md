# Sequences

Stream API rebuilt with OOP principles in mind!
Inspired by sequences in Kotlin standard library.

## Comparing With Stream API

Similarities:

* provides declarative approach to iterate over some values;
* has almost the same set of operations.

Differences:

* sequences do not become consumed once terminal operation invoked;
* parallelism is not supported.

## Example use case

As you can see, Sequences API matches 90% with Stream API, which available since Java 8:

```java
public class Main {
    public static void main(String[] args) {
        var sequence = new ArraySequence<>(1, 2, 3, 4, 5)
                .map(i -> i + 1) // 2 3 4 5 6
                .flatMap(i -> new ArraySequence<>(i, i)) // 2 2 3 3 4 4 5 5 6 6
                .filter(i -> i % 2 == 0) // 2 2 4 4 6 6
                .distinct() // 2 4 6
                .sorted(Comparator.<Integer>naturalOrder().reversed()); // 6 4 2

        System.out.println(sequence.fold(new ListAccumulator<>())); // [6, 4, 2]

        System.out.println(sequence.fold(new ListAccumulator<>())); // [6, 4, 2]
    }
}
```

## Modularity

According to [`module-info.java`](https://github.com/Xini1/sequences/blob/main/src/main/java/module-info.java):

```java
module sequences {

    exports com.github.xini1.accumulator.base;
    exports com.github.xini1.sequence.base;
}
```

only classes in `com.github.xini1.sequence.base` and `com.github.xini1.accumulator.base` exposed to public.

## Main interfaces

Library provides two interfaces to be implemented (if it makes sense) by user.

[`Sequence`](https://github.com/Xini1/sequences/blob/main/src/main/java/com/github/xini1/sequence/base/Sequence.java) -
provides API to manipulate elements in sequence.

[`Accumulator`](https://github.com/Xini1/sequences/blob/main/src/main/java/com/github/xini1/accumulator/base/Accumulator.java) - provides API to convert sequence into some result.

## Shipped implementations of [`Sequence`](https://github.com/Xini1/sequences/blob/main/src/main/java/com/github/xini1/sequence/base/Sequence.java)

There are several predefined implementations to be used:

| Class, implementing [`Sequence`](https://github.com/Xini1/sequences/blob/main/src/main/java/com/github/xini1/sequence/base/Sequence.java)       | Stream API analog                                                                                                                                                       |
|-------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [`ArraySequence`](https://github.com/Xini1/sequences/blob/main/src/main/java/com/github/xini1/sequence/base/ArraySequence.java)                 | `Stream.of(T... values)`                                                                                                                                                |
| [`EmptySequence`](https://github.com/Xini1/sequences/blob/main/src/main/java/com/github/xini1/sequence/base/EmptySequence.java)                 | `Stream.empty()`                                                                                                                                                        |
| [`GeneratedSequence`](https://github.com/Xini1/sequences/blob/main/src/main/java/com/github/xini1/sequence/base/GeneratedSequence.java)         | `Stream.iterate(T seed, UnaryOperator<T> f)`, `Stream.iterate(T seed, Predicate<? super T> hasNext, UnaryOperator<T> next)`, `Stream.generate(Supplier<? extends T> s)` |
| [`IterableSequence`](https://github.com/Xini1/sequences/blob/main/src/main/java/com/github/xini1/sequence/base/IterableSequence.java)           | `Collection.stream()`                                                                                                                                                   |
| [`OptionalSequence`](https://github.com/Xini1/sequences/blob/main/src/main/java/com/github/xini1/sequence/base/OptionalSequence.java)           | `Optional.stream()`                                                                                                                                                     |
| [`SingleElementSequence`](https://github.com/Xini1/sequences/blob/main/src/main/java/com/github/xini1/sequence/base/SingleElementSequence.java) | `Stream.of(T t)`                                                                                                                                                        |

## Shipped implementations of [`Accumulator`](https://github.com/Xini1/sequences/blob/main/src/main/java/com/github/xini1/accumulator/base/Accumulator.java)

Library gives listed below classes:

| Class, implementing [`Accumulator`](https://github.com/Xini1/sequences/blob/main/src/main/java/com/github/xini1/accumulator/base/Accumulator.java)     | Stream API analog                                                                                                                                                                                                                                           |
|--------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [`CollectionAccumulator`](https://github.com/Xini1/sequences/blob/main/src/main/java/com/github/xini1/accumulator/base/CollectionAccumulator.java)     | `Collectors.toCollection(Supplier<C> collectionFactory)`                                                                                                                                                                                                    |
| [`CombiningAccumulator`](https://github.com/Xini1/sequences/blob/main/src/main/java/com/github/xini1/accumulator/base/CombiningAccumulator.java)       | `Collectors.reducing(BinaryOperator<T> op)`, `Stream.reduce(BinaryOperator<T> accumulator)`                                                                                                                                                                 |
| [`GroupingAccumulator`](https://github.com/Xini1/sequences/blob/main/src/main/java/com/github/xini1/accumulator/base/GroupingAccumulator.java)         | `Collectors.groupingBy(Function<? super T, ? extends K> classifier)`                                                                                                                                                                                        |
| [`JoiningAccumulator`](https://github.com/Xini1/sequences/blob/main/src/main/java/com/github/xini1/accumulator/base/JoiningAccumulator.java)           | `Collectors.joining()`, `Collectors.joining(CharSequence delimiter)`, `Collectors.joining(CharSequence delimiter, CharSequence prefix, CharSequence suffix)`                                                                                                |
| [`ListAccumulator`](https://github.com/Xini1/sequences/blob/main/src/main/java/com/github/xini1/accumulator/base/ListAccumulator.java)                 | `Collectors.toList()`                                                                                                                                                                                                                                       |
| [`MapAccumulator`](https://github.com/Xini1/sequences/blob/main/src/main/java/com/github/xini1/accumulator/base/MapAccumulator.java)                   | `Collectors.toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper, BinaryOperator<U> mergeFunction)`, `Collectors.toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper)` |
| [`PartitioningAccumulator`](https://github.com/Xini1/sequences/blob/main/src/main/java/com/github/xini1/accumulator/base/PartitioningAccumulator.java) | `Collectors.partitioningBy(Predicate<? super T> predicate)`                                                                                                                                                                                                 |
| [`ReducingAccumulator`](https://github.com/Xini1/sequences/blob/main/src/main/java/com/github/xini1/accumulator/base/ReducingAccumulator.java)         | `Collectors.reducing(U identity, Function<? super T, ? extends U> mapper, BinaryOperator<U> op)`, `Stream.reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner)`                                                          |
| [`SetAccumulator`](https://github.com/Xini1/sequences/blob/main/src/main/java/com/github/xini1/accumulator/base/SetAccumulator.java)                   | `Collectors.toSet()`                                                                                                                                                                                                                                        |
