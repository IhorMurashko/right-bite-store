package com.best_store.right_bite.constant.bmi.prompts;

import lombok.experimental.UtilityClass;

/**
 * Utility class containing predefined AI prompts to generate recommendations based on user BMI.
 *
 * This class provides a templated prompt to assist AI in creating
 * user-friendly and context-aware product recommendations tailored to
 * the user's computed Body Mass Index (BMI) category.
 *
 * <p>This is a helper class and should not be instantiated. The predefined
 * prompts are used as input for systems that generate personalized recommendations.</p>
 *
 * Usage example:
 * <pre>{@code
 * String promptRequest = String.format(
 *     AiRequestPrompts.BMI_SELECTION_PRODUCTS_RECOMMENDATION_PROMPT,
 *     bmiCategory.name(), productSuggestedNames
 * );
 * }</pre>
 *
 * @author Ihor Murashko
 */
@UtilityClass
public class AiRequestPrompts {
    public final String BMI_SELECTION_PRODUCTS_RECOMMENDATION_PROMPT = """
            You are a nutrition assistant for an online healthy food store.
            The user has calculated their Body Mass Index (BMI) and he has %s.
            Based on it, a selection of recommended products has been retrieved from the store's database.
            
            Here is the list of products to consider:
            %s
            
            Please write a short and friendly recommendation (2–3 sentences) for the user.
            - Mention some of the products from the list in a natural way.
            - Adjust the message depending on the BMI category (e.g., underweight → suggest higher-calorie foods, overweight → suggest lower-calorie foods).
            - Include a simple lifestyle tip, but keep it general (e.g., balanced diet, physical activity, hydration).
            - Keep the tone supportive and encouraging, not medical or prescriptive.
            - **Do NOT include a closing phrase, signature, or "Best, [Your Name]". Only provide the recommendation text.**
            """;
}
