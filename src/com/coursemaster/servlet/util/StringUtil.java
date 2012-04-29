package com.coursemaster.servlet.util;

public class StringUtil {
    /**
     * Returns a capitalized version of a string
     * abc becomes Abc
     *
     * @param string The string
     * @return Capitalized string
     */
    public static String capitalize(String string) {
        char[] chars = string.toCharArray();
        // Only operate on first character,
        // Ensuring it is in fact a letter in
        // the a-z range
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            chars[0] = Character.toUpperCase(chars[0]);
        }
        return new String(chars);
    }
}
