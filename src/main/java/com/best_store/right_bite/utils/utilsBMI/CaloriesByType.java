package com.best_store.right_bite.utils.utilsBMI;

import com.best_store.right_bite.constans.BMICategory;
import lombok.experimental.UtilityClass;

/**
 * Utility class to get the recommended daily calorie intake based on BMI category.
 */
@UtilityClass
public class CaloriesByType {
    public int getCaloriesByType(BMICategory bmiCategory) {
        return switch (bmiCategory) {
            case UNDERWEIGHT -> 270;
            case NORMAL_WEIGHT -> 220;
            case OVERWEIGHT -> 180;
        };
    }
}
