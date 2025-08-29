package com.best_store.right_bite.dto.dtoBMI;

import com.best_store.right_bite.dto.catalog.ProductDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * BmiResponse is a record that encapsulates the response of a BMI calculation.
 *
 * @param bmi        - the calculated Body Mass Index (BMI) value
 * @param category   - the BMI category based on the calculated BMI value
 * @param healthRisk - a string describing the health risk associated with the BMI category
 * @param aiResponse - a response from AI of health recommendations based on the BMI category given by AI
 * @param items      - a list of products suggested based on the BMI category
 */
@Schema(name = "BMI Response", description = "Contains BMI calculation results and recommendations")
public record BmiResponse(
        @Schema(description = "Calculated Body Mass Index value", example = "22.5")
        double bmi,
        @Schema(description = "BMI category based on calculated value",
                example = "Normal",
                allowableValues = {"Underweight", "Normal", "Overweight"})
        String category,
        @Schema(description = "Health risks associated with the BMI category",
                example = "Being in a normal weight range reduces risks of various health conditions")
        String healthRisk,
        @Schema(description = "AI-generated personalized recommendations for lifestyle and dietary changes",
                example = "Based on your BMI category, here are some recommendations: ...")
        String aiResponse,
        @Schema(description = "List of recommended products based on BMI category")
        List<ProductDTO> items
) {
}
