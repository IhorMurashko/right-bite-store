package com.best_store.right_bite.bmiInterpreter;


import com.best_store.right_bite.constant.bmi.BMICategory;

/**
 * TeenGirlBmiInterpreterBMI is an implementation of the InterpreterBMI interface
 * that interprets BMI values for teen girls according to standard BMI categories.
 * It categorizes the BMI into four categories: Underweight, Normal weight,
 * Overweight, and Obese.
 */
public class TeenGirlBmiInterpreterBMI implements InterpreterBMI {
    @Override
    public BMICategory interpret(double bmi) {
        if (bmi < 17) return BMICategory.UNDERWEIGHT;
        if (bmi < 22.5) return BMICategory.NORMAL_WEIGHT;
        return BMICategory.OVERWEIGHT;
    }
}
