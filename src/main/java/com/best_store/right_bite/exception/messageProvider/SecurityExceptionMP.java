package com.best_store.right_bite.exception.messageProvider;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityExceptionMP {
    public final String TOKEN_ACCESS_EXCEPTION = "Token with type %s can't be used for access for this endpoint.";
    public final String INVALID_TOKEN = "Invalid token.";
    public final String AUTHENTICATION_CAST_INSTANCE_CAST_EXCEPTION = "Authentication principal is not instance of JwtPrincipal.";
    public final String INVALID_TOKEN_SUBJECT = "Invalid token subject. Subject couldn't be %s type.";
}
