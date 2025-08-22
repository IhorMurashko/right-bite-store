package com.best_store.right_bite.utils.utilsBMI;

import com.best_store.right_bite.constant.bmi.BMICategory;
import com.best_store.right_bite.constant.healthRiskEsteem.HealthRiskEsteemRecommendations;
import lombok.experimental.UtilityClass;

/**
 * Utility class for estimating health risks based on BMI category.
 * Provides recommendations aligned with the BMI classification.
 * The class uses pre-defined strings for health risk messages.
 *
 * <p>Example use-case:</p>
 * <pre>
 *     BMICategory bmiCategory = BMICategory.NORMAL_WEIGHT;
 *     String healthRisk = HealthRiskEsteem.calculateHealthRisk(bmiCategory);
 *     System.out.println(healthRisk);
 * </pre>
 *
 * @author Ihor Murashko
 */
@UtilityClass
public class HealthRiskEsteem {
    public String calculateHealthRisk(BMICategory bmi) {
        if (bmi == BMICategory.UNDERWEIGHT) {
            return HealthRiskEsteemRecommendations.FOR_UNDERWEIGHT;
        } else if (bmi == BMICategory.NORMAL_WEIGHT) {
            return HealthRiskEsteemRecommendations.FOR_NORMAL_WEIGHT;
        } else {
            return HealthRiskEsteemRecommendations.FOR_OVERWEIGHT;
        }
    }
}
