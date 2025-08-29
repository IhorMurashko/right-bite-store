package com.best_store.right_bite.constant.healthRiskEsteem;

import lombok.experimental.UtilityClass;

/**
 * Provides pre-defined health risk recommendations based on BMI categories.
 *
 * This utility class contains constant strings representing the health risks and
 * advice for individuals categorized as underweight, normal weight, or overweight.
 * These constants can be used to deliver personalized recommendations.
 *
 * @author Ihor Murashko
 */
@UtilityClass
public class HealthRiskEsteemRecommendations {
    public final String FOR_UNDERWEIGHT = "You are underweight, which can lead to nutritional deficiencies and weakened immune function.";
    public final String FOR_NORMAL_WEIGHT = "You have a normal weight, which is generally associated with lower health risks.";
    public final String FOR_OVERWEIGHT = "You are overweight, which may increase your risk of heart disease, diabetes, and other health issues.";
}
