package com.best_store.right_bite.service.serviceBMI.bmiAiCallers;

import com.best_store.right_bite.exception.ExceptionMessageProvider;
import com.best_store.right_bite.exception.ai.OpenAiCallerException;
import com.best_store.right_bite.service.aiCallers.AiBaseCaller;
import com.best_store.right_bite.service.serviceBMI.BMIServiceImpl;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Component;

/**
 * BmiGroqCaller is a service class responsible for interacting with the OpenAI Groq chat model.
 * It enables sending prompts to the AI model and retrieving AI-generated recommendations
 * and insights based on the given input.
 * <p>
 * This class is used as a part of the BMI-related services to generate personalized recommendations
 * using AI based on factors like BMI, age, and gender.
 * <p>
 * Exceptions related to AI interaction are encapsulated and transformed into domain-level exceptions.
 *
 * <p>
 * Best practices:
 * - Ensure valid input is provided to the {@link #callPrompt(String)} method.
 * - Handle exceptions gracefully, as the class already encapsulates the error handling mechanism.
 * </p>
 *
 * <p>
 * Dependencies:
 * This class depends on {@link OpenAiChatModel} for calling the AI services.
 * </p>
 *
 * @author Ihor Murashko
 * @see OpenAiChatModel
 * @see AiBaseCaller
 * @see ExceptionMessageProvider
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BmiGroqCaller implements AiBaseCaller<String> {

    private final OpenAiChatModel groqChatClient;

    /**
     * BmiGroqCaller is a service class responsible for interacting with the OpenAI Groq chat model.
     * It enables sending prompts to the AI model and retrieving AI-generated responses
     * tailored for BMI-related recommendations.
     * <p>
     * This class integrates AI services into the BMI-related application flow and ensures
     * consistent error handling for any AI-related communication issues.
     * </p>
     *
     * <p>
     * Usage:
     * - Call {@link #callPrompt(String)} to pass a prompt to the Groq chat model and retrieve a response.
     * - Ensure input prompt strings are not null to avoid validation issues.
     * </p>
     *
     * @see OpenAiChatModel
     * @see AiBaseCaller
     * @see ExceptionMessageProvider
     * @see OpenAiCallerException
     * @see BMIServiceImpl
     */
    @Override
    public String callPrompt(@NotNull String prompt) {
        try {
            log.debug("Calling groq chat model with prompt: {}", prompt);
            return groqChatClient.call(prompt);
        } catch (RuntimeException exception) {
            log.error("Error occurred while calling groq chat model: {}", exception.getMessage());
            throw new OpenAiCallerException(
                    String.format(ExceptionMessageProvider.INTERNAL_AI_CALL_ERROR, exception.getMessage()));
        }
    }
}
