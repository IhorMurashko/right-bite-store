package com.best_store.right_bite.utils.user;


import lombok.experimental.UtilityClass;
import org.mapstruct.Named;

import java.util.Arrays;
import java.util.stream.Collectors;

@UtilityClass
public class UserFieldAdapter {

    /**
     * Formats a field to lower case
     *
     * Examples:
     *  "myEmaIL@gMAIL.COM" -> "myemail@gmail.com"
     */
    @Named("toLower")
    public String toLower(String field) {
        return field.toLowerCase();
    }


    /**
     * Formats a name (first or last) to:
     * - Capitalize the first letter of each word
     * - Remove duplicate spaces or hyphens
     * - Lowercase other characters
     *
     * Examples:
     *   "JOHN" -> "John"
     *   "maRY-ann" -> "Mary-Ann"
     *   "алЕКСЕЙ   иВАНОВ" -> "Алексей Иванов"
     */
    @Named("normalizeName")
    public String normalizeName(String raw) {
        if (raw == null || raw.isBlank()) return raw;

        return Arrays.stream(raw.trim().split("[\\s\\-]+"))
                .filter(s -> !s.isBlank())
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "))
                .replaceAll("\\s+", " ")
                .replaceAll("\\-+", "-");
    }

    /**
     * Formats address parts like city, country, or street to be capitalized and cleaned.
     * Each word's first letter becomes uppercase, the rest lowercase.
     *
     * Example: "new   YORK" -> "New York"
     */
    @Named("normalizeAddress")
    public String normalizeAddress(String raw) {
        return normalizeName(raw);
    }

    /**
     * Removes all non-digit characters from a phone number.
     * Useful before validation or storage.
     *
     * Example: "+1 (234) 567-8900" -> "12345678900"
     */
    @Named("sanitizePhoneNumber")
    public String sanitizePhoneNumber(String raw) {
        if (raw == null) return null;
        return raw.replaceAll("\\D", "");
    }

    /**
     * Normalizes ZIP/postal code:
     * - Removes all non-alphanumeric characters
     * - Converts to uppercase
     *
     * Example: " ab-1234 " -> "AB1234"
     */
    @Named("normalizeZipCode")
    public String normalizeZipCode(String raw) {
        if (raw == null) return null;
        return raw.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
    }
}