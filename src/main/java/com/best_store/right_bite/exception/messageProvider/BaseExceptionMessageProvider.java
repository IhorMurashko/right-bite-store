package com.best_store.right_bite.exception.messageProvider;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BaseExceptionMessageProvider {
    public final String INTERNAL_AI_CALL_ERROR = "Internal AI call error: %s";
    public final String OPTIMISTIC_LOCKING_EXCEPTION = "Optimistic locking exception for user id: %d";
}
