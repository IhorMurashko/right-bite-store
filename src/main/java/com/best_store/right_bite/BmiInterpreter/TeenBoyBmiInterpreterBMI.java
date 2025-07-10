package com.best_store.right_bite.BmiInterpreter;

import com.best_store.right_bite.constans.BMICategory;

/**
 * TeenBoyBmiInterpreterBMI is an implementation of the InterpreterBMI interface
 * that interprets BMI values for teen boys according to standard BMI categories.
 * It categorizes the BMI into four categories: Underweight, Normal weight,
 * Overweight, and Obese.
 */
public class TeenBoyBmiInterpreterBMI implements InterpreterBMI {
    @Override
    public BMICategory interpret(double bmi) {
        if (bmi < 17.5) return BMICategory.UNDERWEIGHT;
        if (bmi < 23.0) return BMICategory.NORMAL_WEIGHT;
        return BMICategory.OVERWEIGHT;
    }
}
