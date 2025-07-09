package com.best_store.right_bite.BmiInterpreter;

import com.best_store.right_bite.constans.BMICategory;

/**
 * AdultBmiInterpreterBMI is an implementation of the InterpreterBMI interface
 * that interprets BMI values for adults according to standard BMI categories.
 * It categorizes the BMI into four categories: Underweight, Normal weight,
 * Overweight, and Obese.
 */
public class AdultBmiInterpreterBMI implements InterpreterBMI {


    @Override
    public BMICategory interpret(double bmi) {
        if (bmi < 18.5) return BMICategory.UNDERWEIGHT;
        if (bmi < 25.0) return BMICategory.NORMAL_WEIGHT;
        if (bmi < 30.0) return BMICategory.OVERWEIGHT;
        return BMICategory.OBESE;
    }
}