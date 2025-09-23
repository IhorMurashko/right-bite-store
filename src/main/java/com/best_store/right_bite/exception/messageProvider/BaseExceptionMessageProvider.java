package com.best_store.right_bite.exception.messageProvider;

import lombok.experimental.UtilityClass;

/**
 * BaseExceptionMessageProvider is a utility class that provides core predefined base-level exception messages
 * utilized across various application components.
 *
 * <p>This class helps centralize commonly used exception messages, ensuring reusable, consistent, and
 * maintainable error messaging standards. It is particularly useful for common exceptions such as
 * internal errors or optimistic locking failures.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * throw new InternalError(String.format(BaseExceptionMessageProvider.INTERNAL_AI_CALL_ERROR, additionalInfo));
 * }</pre>
 *
 * <p>Designed as a final utility class to prevent instantiation and inheritance.</p>
 *
 * @author Ihor Murashko
 */
@UtilityClass
public class BaseExceptionMessageProvider {
    public final String INTERNAL_AI_CALL_ERROR = "Internal AI call error: %s";
    public final String OPTIMISTIC_LOCKING_EXCEPTION = "Optimistic locking exception for user id: %d";
}
