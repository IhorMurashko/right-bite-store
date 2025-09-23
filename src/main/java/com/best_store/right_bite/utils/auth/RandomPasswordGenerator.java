package com.best_store.right_bite.utils.auth;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;

/**
 * Utility class for generating random passwords.
 * <p>
 * This class provides a method to generate random passwords consisting of alphanumeric characters.
 * It is designed to be stateless and thread-safe.
 * </p>
 *
 * <p><strong>Usage Example:</strong></p>
 * <pre>{@code
 * String password = RandomPasswordGenerator.generatePassword();
 * }</pre>
 *
 * @author Ihor Murashko
 */
@UtilityClass
public class RandomPasswordGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();

    /**
     * Generates a random password consisting of alphanumeric characters.
     * <p>
     * The password is generated using a secure random number generator and has
     * a fixed length defined by the constant {@code PASSWORD_LENGTH}.
     * </p>
     *
     * @return a randomly generated password as a {@link String}.
     * @author Ihor Murashko
     */
    public String generatePassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }
        return password.toString();
    }
}