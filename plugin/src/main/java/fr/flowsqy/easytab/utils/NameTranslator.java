package fr.flowsqy.easytab.utils;

import java.util.Arrays;
import java.util.Comparator;

import org.jetbrains.annotations.NotNull;

public class NameTranslator {

    private final char[] letters;
    private final byte nameLength;
    private final long maxValue;

    public NameTranslator(int nameLength, @NotNull Comparator<Character> comparator) {
        letters = generateLetters(comparator);
        this.nameLength = (byte) nameLength;
        maxValue = pow(letters.length, nameLength) - 1;
    }

    private char[] generateLetters(@NotNull Comparator<Character> comparator) {
        final Character[] letters = new Character[26 * 2 + 10];
        for (char offset = 0, letter = 'a'; offset < 26; offset++, letter++) {
            letters[offset] = letter;
        }
        for (char offset = 26, letter = 'A'; offset < 52; offset++, letter++) {
            letters[offset] = letter;
        }
        for (char offset = 52, letter = '0'; offset < 62; offset++, letter++) {
            letters[offset] = letter;
        }
        Arrays.sort(letters, comparator);
        final char[] primitiveLetters = new char[letters.length];
        for (int index = 0; index < letters.length; index++) {
            primitiveLetters[index] = letters[index].charValue();
        }
        return primitiveLetters;
    }

    private long pow(int baseValue, int exponent) {
        long value = 1;
        for (int i = 0; i < exponent; i++) {
            value *= baseValue;
        }
        return value;
    }

    public long getMaxValue() {
        return maxValue;
    }

    public String translate(long value) {
        if (value > maxValue) {
            throw new IllegalArgumentException(value + " excess the maximum value (" + maxValue + ")");
        }
        final char[] buffer = new char[nameLength];
        int index = nameLength;
        while (value > 0) {
            buffer[--index] = letters[(int) (value % letters.length)];
            value /= letters.length;
        }
        while (index > 0) {
            buffer[--index] = letters[0];
        }
        return new String(buffer);
    }

}
