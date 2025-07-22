package com.best_store.right_bite.constant.constraint;


import lombok.experimental.UtilityClass;

/**
 * Utility class providing regular expression patterns and associated error messages
 * for validating user input fields related to the {@code User} entity and related data.
 *
 * <p>This class is designed to be used both for manual validation logic and as pattern
 * definitions inside validation annotations such as {@code @Pattern}.</p>
 *
 * <p>Patterns include validation for email, password, names, phone numbers,
 * addresses, house numbers, zip codes, and payment card details.</p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>
 * &#64;Pattern(regexp = UserFieldsConstraint.EMAIL_PATTERN, message = UserFieldsConstraint.EMAIL_MESSAGE)
 * private String email;
 * </pre>
 *
 * <p><b>Author:</b> Ihor Murashko</p>
 */
@UtilityClass
public class UserFieldsConstraint {

    /**
     * Email must match standard email format (RFC 5322 simplified).
     */
    public static final String EMAIL_PATTERN = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    public static final String EMAIL_MESSAGE = "Invalid email format";

    /**
     * Password must be at least 4 characters long and contain letters and either digits or special characters.
     */
    public static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*[0-9!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/]).{4,}$";
    public static final String PASSWORD_MESSAGE = "Password must be at least 4 characters long and contain letters and digits or special characters";

    /**
     * Names (first and last) must be at least 2 characters, consist of Latin or Cyrillic letters,
     * and may include spaces or hyphens.
     */
    public static final String NAME_PATTERN = "^(?=.{2,}$)[A-Za-zА-Яа-яЁё]+(?:[-\\s][A-Za-zА-Яа-яЁё]+)*$";
    public static final String NAME_MESSAGE = "Name must contain only letters (Latin or Cyrillic), be at least 2 characters, and may include spaces or hyphens";

    /**
     * Phone number must be 9 to 15 digits long.
     */
    public static final String PHONE_PATTERN = "^[0-9]{9,15}$";
    public static final String PHONE_MESSAGE = "Phone number must contain only digits and be 9 to 15 characters long";

    /**
     * Country, city, and street follow the same pattern rules as names.
     */
    public static final String ADDRESS_PATTERN = NAME_PATTERN;
    public static final String ADDRESS_MESSAGE = "Field must contain only letters (Latin or Cyrillic), be at least 2 characters, and may include spaces or hyphens";

    /**
     * House number must be alphanumeric, 1 to 10 characters (e.g., "10", "12A", "B4").
     */
    public static final String HOUSE_NUMBER_PATTERN = "^[A-Za-z0-9\\-]{1,10}$";
    public static final String HOUSE_NUMBER_MESSAGE = "House number must be alphanumeric (e.g., 10, 12A, B4)";

    /**
     * ZIP code must be 4 to 10 alphanumeric characters.
     */
    public static final String ZIP_CODE_PATTERN = "^[A-Za-z0-9\\-]{4,10}$";
    public static final String ZIP_CODE_MESSAGE = "ZIP code must be 4 to 10 alphanumeric characters";

    /**
     * Card number must be exactly 16 digits.
     */
    public static final String CARD_NUMBER_PATTERN = "^\\d{16}$";
    public static final String CARD_NUMBER_MESSAGE = "Card number must be exactly 16 digits (e.g., 1234567812345678)";

    /**
     * Name on card must contain at least first and last name,
     * consisting of capitalized Latin letters separated by a space.
     */
    public static final String NAME_ON_CARD_PATTERN = "^[A-Z][a-z]+\\s[A-Z][a-z]+(?:\\s[A-Z][a-z]+)*$";
    public static final String NAME_ON_CARD_MESSAGE = "Name on card must contain at least first and last name (e.g., John Doe)";

    /**
     * Expiry date on card must be in MM/YY format, with month from 01 to 12.
     */
    public static final String EXPIRE_DATE_PATTERN = "^(0[1-9]|1[0-2])/\\d{2}$";
    public static final String EXPIRE_DATE_MESSAGE = "Expire date must be in MM/YY format (e.g., 09/27)";
}
