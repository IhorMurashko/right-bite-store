package com.best_store.right_bite.utils.utilsBMI;


import lombok.experimental.UtilityClass;

/**
 * Utility class to calculate Body Mass Index (BMI).
 */
@UtilityClass
public class BmiCalculator {

    public double calculate(double weightKg, double heightCm) {
        double heightMeters = heightCm / 100.0;
        double rawBmi = weightKg / Math.pow(heightMeters, 2);
        return Math.round(rawBmi * 100.0) / 100.0;
    }
}
