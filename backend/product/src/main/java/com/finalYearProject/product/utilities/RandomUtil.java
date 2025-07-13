package com.finalYearProject.product.utilities;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Random;

public class RandomUtil {

    private static final int LEFT_LIMIT = 48; // numeral '0'
    private static final int RIGHT_LIMIT = 122; // letter 'z'
    private static final SecureRandom random = new SecureRandom();

    public static String randomAlpNumString(int size) {
        return random.ints(LEFT_LIMIT, RIGHT_LIMIT + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(size)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString()
                .toUpperCase(Locale.ROOT);

    }

    public static String randomNumString(int size) {
        Random random = new Random();
        return String.format("%0" + size + "d", random.nextInt(((int) Math.pow(10, size)) - 1));
    }
}