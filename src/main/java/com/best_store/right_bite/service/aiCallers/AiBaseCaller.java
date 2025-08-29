package com.best_store.right_bite.service.aiCallers;


import jakarta.validation.constraints.NotNull;

/**
 * Represents a functional interface that defines a single method for interacting with an AI model
 * using a given input prompt. Implementations of this interface should handle the processing of the
 * prompt and return the expected result.
 *
 * @param <T> the type of the result returned after processing the input prompt
 */
@FunctionalInterface
public interface AiBaseCaller<T> {
    T callPrompt(@NotNull String prompt);
}
