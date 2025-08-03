package com.best_store.right_bite.utils.utilsBMI;

import com.best_store.right_bite.constant.bmi.BMICategory;
import lombok.experimental.UtilityClass;

/**
 * Utility class to estimate health risks based on BMI category.
 */
@UtilityClass
public class HealthRiskEstim {
    public String calculateHealthRisk(BMICategory bmi) {
        if (bmi == BMICategory.UNDERWEIGHT) {
            return "You are underweight, which can lead to nutritional deficiencies and weakened immune function.";
        } else if (bmi == BMICategory.NORMAL_WEIGHT) {
            return "You have a normal weight, which is generally associated with lower health risks.";
        } else {
            return "You are overweight, which may increase your risk of heart disease, diabetes, and other health issues.";
        }
    }
}
