package com.best_store.right_bite.constant.bmi;


import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * Enum representing different BMI categories.
 * Each category corresponds to a specific range of BMI values.
 */
@Getter
public enum BMICategory {
    UNDERWEIGHT("Underweight"),
    NORMAL_WEIGHT("Normal"),
    OVERWEIGHT("Overweight");


    private final String category;

    BMICategory(String category) {
        this.category = category;
    }


}