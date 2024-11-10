package com.challenge.operations.generator;

import java.security.SecureRandom;

/**
 * Utility class for generating random strings using a predefined set of characters.
 */
public class RandomStringGenerator {

    /**
     * A string containing characters used for generating random strings.
     * This includes all uppercase and lowercase letters, as well as digits from 0 to 9.
     */
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    /**
     * A thread-safe and cryptographically strong random number generator.
     * Utilized to generate secure random values for creating random strings.
     */
    private static final SecureRandom random = new SecureRandom();

    /**
     * Generates a random string of a specified length using a predefined set of characters.
     *
     * @param length the desired length of the generated string
     * @return a randomly generated string of the specified length
     */
    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }
}

