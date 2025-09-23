package com.best_store.right_bite.exception.messageProvider;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthExceptionMP {
    public final String USER_ACCOUNT_IS_EXPIRED = "User account is expired.";
    public final String PASSWORDS_DONT_MATCH = "Passwords don't match";
    public final String EMAIL_ALREADY_EXIST = "Email: %s already exist";
}
