package com.best_store.right_bite.exception.messageProvider;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserExceptionMP {
    public final String USER_EMAIL_NOT_FOUND = "User with email: %s not found";
    public final String USER_ID_NOT_FOUND = "User with ID: %d not found";
}
