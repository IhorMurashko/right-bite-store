package com.best_store.right_bite.security.exception;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityExceptionMessageProvider {
    public final String TOKEN_IS_NOT_VALID
            = "token is not valid";
    public final String TOKEN_WAS_REVOKED
            = "token was revoked";
    public final String INVALID_TOKEN_TYPE
            = "invalid token type %s";
    public final String ACCESS_DENIED_EXCEPTION_MESSAGE
            = "Error: Access denied";
    public final String UNAUTHORIZED_EXCEPTION_MESSAGE
            = "Error: Unauthorized";
    public static final String TOKEN_RESPONSE_TEMPLATE = "{\"error\": \"%s\"}";

}
