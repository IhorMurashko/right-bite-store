package com.best_store.right_bite.dto.dtoBMI;

import com.best_store.right_bite.constant.bmi.BMICategory;
import com.best_store.right_bite.dto.catalog.ProductDTO;

import java.util.List;

/**
 * BmiResponse is a record that encapsulates the response of a BMI calculation.
 * @param bmi - the calculated Body Mass Index (BMI) value
 * @param category - the BMI category based on the calculated BMI value
 * @param healthRisk - a string describing the health risk associated with the BMI category
 * @param recomendationsList - a list of health recommendations based on the BMI category given by AI
 * @param items - a list of products suggested based on the BMI category
 */
public record BmiResponse(
        double bmi,
        BMICategory category,
        String healthRisk,
        List<String> recomendationsList,
        List<ProductDTO> items
) {
}
