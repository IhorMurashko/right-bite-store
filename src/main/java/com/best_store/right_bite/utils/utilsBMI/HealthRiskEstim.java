package com.best_store.right_bite.utils.utilsBMI;

import com.best_store.right_bite.constans.BMICategory;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HealthRiskEstim {
    public String calculateHealthRisk(BMICategory bmi) {
        if (bmi == BMICategory.UNDERWEIGHT) {
            return "You are underweight, which can lead to nutritional deficiencies and weakened immune function.";
        } else if (bmi == BMICategory.NORMAL_WEIGHT) {
            return "You have a normal weight, which is generally associated with lower health risks.";
        } else if (bmi == BMICategory.OVERWEIGHT) {
            return "You are overweight, which may increase your risk of heart disease, diabetes, and other health issues.";
        } else {
            return "You are obese, which significantly increases your risk of serious health conditions such as heart disease, diabetes, and certain cancers.";
        }
    }
}
