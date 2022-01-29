package org.example.accumulator.base;

/**
 * @author Maxim Tereshchenko
 */
public final class JoiningAccumulator<T extends CharSequence> implements Accumulator<T, String> {

    private final String valueIfNoElements;
    private final CharSequence prefix;
    private final CharSequence delimiter;
    private final CharSequence suffix;

    public JoiningAccumulator(
            String valueIfNoElements,
            CharSequence prefix,
            CharSequence delimiter,
            CharSequence suffix
    ) {
        this.valueIfNoElements = valueIfNoElements;
        this.prefix = prefix;
        this.delimiter = delimiter;
        this.suffix = suffix;
    }

    public JoiningAccumulator(CharSequence prefix, CharSequence delimiter, CharSequence suffix) {
        this(prefix.toString() + suffix, prefix, delimiter, suffix);
    }

    public JoiningAccumulator(CharSequence delimiter) {
        this("", delimiter, "");
    }

    @Override
    public Accumulator<T, String> onElement(T element) {
        return new NotEmptyJoiningAccumulator<>(element.toString(), prefix, delimiter, suffix);
    }

    @Override
    public String onFinish() {
        return valueIfNoElements;
    }

    private static final class NotEmptyJoiningAccumulator<T extends CharSequence> implements Accumulator<T, String> {

        private final String joined;
        private final CharSequence prefix;
        private final CharSequence delimiter;
        private final CharSequence suffix;

        private NotEmptyJoiningAccumulator(
                String joined,
                CharSequence prefix,
                CharSequence delimiter,
                CharSequence suffix
        ) {
            this.joined = joined;
            this.prefix = prefix;
            this.delimiter = delimiter;
            this.suffix = suffix;
        }

        @Override
        public Accumulator<T, String> onElement(T element) {
            return new NotEmptyJoiningAccumulator<>(joined + delimiter + element, prefix, delimiter, suffix);
        }

        @Override
        public String onFinish() {
            return prefix + joined + suffix;
        }
    }
}
