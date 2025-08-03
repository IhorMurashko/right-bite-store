package com.best_store.right_bite.bmiInterpreter;

import com.best_store.right_bite.constant.bmi.BMICategory;

/**
 * SeniorBmiInterpreterBMI is an implementation of the InterpreterBMI interface
 * that interprets BMI values for seniors according to standard BMI categories.
 * It categorizes the BMI into four categories: Underweight, Normal weight,
 * Overweight, and Obese.
 */
public class SeniorBmiInterpreterBMI implements InterpreterBMI {
    @Override
    public BMICategory interpret(double bmi) {
        if (bmi < 22.0) return BMICategory.UNDERWEIGHT;
        if (bmi < 27.0) return BMICategory.NORMAL_WEIGHT;
        return BMICategory.OVERWEIGHT;
    }
}
