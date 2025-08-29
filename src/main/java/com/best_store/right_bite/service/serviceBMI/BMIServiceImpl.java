package com.best_store.right_bite.service.serviceBMI;

import com.best_store.right_bite.bmiInterpreter.BmiInterpreterFactory;
import com.best_store.right_bite.bmiInterpreter.InterpreterBMI;
import com.best_store.right_bite.constant.bmi.BMICategory;
import com.best_store.right_bite.constant.bmi.prompts.AiRequestPrompts;
import com.best_store.right_bite.dto.catalog.ProductDTO;
import com.best_store.right_bite.dto.dtoBMI.BmiRequest;
import com.best_store.right_bite.dto.dtoBMI.BmiResponse;
import com.best_store.right_bite.service.catalog.productDomain.ProductService;
import com.best_store.right_bite.service.serviceBMI.bmiAiCallers.BmiGroqCaller;
import com.best_store.right_bite.utils.utilsBMI.BmiCalculator;
import com.best_store.right_bite.utils.utilsBMI.HealthRiskEsteem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;


/**
 * BMIServiceImpl here all computations are called.
 * It calculates the Body Mass Index (BMI) based on the provided request,
 * interprets the BMI category and provides health recommendations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class BMIServiceImpl implements BMIService {

    private final ProductService productService;

    /**
     * The Groq is used to get recommendations based on the BMI category.
     */
    private final BmiGroqCaller groqCaller;

    /**
     * Calculates the Body Mass Index (BMI) based on the provided request.
     *
     * @param bmiRequest the request containing weight, age, gender and height
     * @return a response containing the calculated BMI, category, health risk, recommendations, our products
     */
    @Override
    public BmiResponse calculateBmi(@Valid @NotNull BmiRequest bmiRequest) {
        double bmi = BmiCalculator.calculate(bmiRequest.weightKg(), bmiRequest.heightCm());
        InterpreterBMI interpreter = BmiInterpreterFactory
                .getInterpreter(bmiRequest.gender(), bmiRequest.age());
        BMICategory bmiCategory = interpreter.interpret(bmi);
        log.info("BMI category: {}", bmiCategory);
        List<ProductDTO> productsByBMICategory = productService.getProductsByBMICategory(bmiCategory);
        String productSuggestedNames = productsByBMICategory.stream()
                .limit(5)
                .map(product -> "- " + product.getProductName())
                .collect(Collectors.joining("\n"));
        log.debug("Products suggested names:{}", productSuggestedNames);
        String promptRequest = String.format(
                AiRequestPrompts.BMI_SELECTION_PRODUCTS_RECOMMENDATION_PROMPT,
                bmiCategory.name(), productSuggestedNames
        );
        log.debug("Prompt request:\n{}", promptRequest);
        String recommendations = groqCaller.callPrompt(promptRequest);
        log.debug("ai recommendations response:\n{}", recommendations);
        String healthRisk = HealthRiskEsteem.calculateHealthRisk(bmiCategory);
        log.info("Health conditions: {}", healthRisk);
        return new BmiResponse(bmi, bmiCategory.getCategory(), healthRisk, recommendations,
                productsByBMICategory.stream().limit(5).toList());
    }
}
