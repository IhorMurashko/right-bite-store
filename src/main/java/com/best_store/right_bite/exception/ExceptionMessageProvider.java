package com.best_store.right_bite.exception;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessageProvider {
    public final String USER_EMAIL_NOT_FOUND = "User with email: %s not found";
    public final String USER_ID_NOT_FOUND = "User with ID: %d not found";
    public final String PASSWORDS_DONT_MATCH = "Passwords don't match";
    public final String EMAIL_ALREADY_EXIST = "Email: %s already exist";
    public final String INVALID_CREDENTIALS = "Invalid email or password.";
    public final String USER_ACCOUNT_IS_EXPIRED = "User account is expired.";
    public final String NOTIFICATION_CHANNEL_WAS_NOT_FOUND = "Notification type %s was not found";
    public final String NOTIFICATION_BUILDER_WAS_NOT_FOUND = "Notification builder with type: %s and channel: %s was not found.";
    public final String TOKEN_ACCESS_EXCEPTION = "Token with type %s can't be used for access for this endpoint.";
    public final String INVALID_TOKEN = "Invalid token.";
    public final String AUTHENTICATION_CAST_INSTANCE_CAST_EXCEPTION= "Authentication principal is not instance of JwtPrincipal.";
    public final String INVALID_TOKEN_SUBJECT= "Invalid token subject. Subject couldn't be %s type.";


}
