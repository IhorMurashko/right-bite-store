package com.best_store.right_bite.exception;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessageProvider {
    public static final String USER_EMAIL_NOT_FOUND = "User with email: %s not found";
    public static final String USER_ID_NOT_FOUND = "User with ID: %d not found";
    public static final String PASSWORDS_DONT_MATCH = "Passwords don't match";
    public static final String EMAIL_ALREADY_EXIST = "Email: %s already exist";
    public static final String INVALID_CREDENTIALS = "Invalid email or password.";
    public static final String REQUEST_HEADER_DOES_NOT_PRESENT = "Request doesn't contains header: %s";
}
