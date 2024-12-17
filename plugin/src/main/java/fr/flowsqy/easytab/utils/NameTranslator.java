package fr.flowsqy.easytab.utils;

import java.util.Arrays;
import java.util.Comparator;

public class NameTranslator {

    private final static char[] LETTERS = generateLetters();

    private final static char[] generateLetters() {
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
        final Comparator<Character> comparator = Comparator.comparing(String::valueOf,
                String.CASE_INSENSITIVE_ORDER);
        Arrays.sort(letters, comparator);
        final char[] primitiveLetters = new char[letters.length];
        for (int index = 0; index < letters.length; index++) {
            primitiveLetters[index] = letters[index].charValue();
        }
        return primitiveLetters;
    }

    public String translate(long value) {
        // Classic number to zero division
        return null;
    }

}
