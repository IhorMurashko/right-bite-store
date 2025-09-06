package com.best_store.right_bite.service.serviceBMI;

import com.best_store.right_bite.constant.bmi.BMICategory;
import com.best_store.right_bite.constant.bmi.Gender;
import com.best_store.right_bite.constant.constraint.bmi.BmiConstraints;
import com.best_store.right_bite.constant.healthRiskEsteem.HealthRiskEsteemRecommendations;
import com.best_store.right_bite.dto.catalog.ProductDTO;
import com.best_store.right_bite.dto.dtoBMI.BmiRequest;
import com.best_store.right_bite.dto.dtoBMI.BmiResponse;
import com.best_store.right_bite.service.catalog.productDomain.ProductService;
import com.best_store.right_bite.service.serviceBMI.bmiAiCallers.BmiGroqCaller;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BMIServiceImplTest {

    @Mock
    private BmiGroqCaller groqCaller;
    @Mock
    private ProductService productService;
    @InjectMocks
    private BMIServiceImpl bmiService;

    private static Validator validator;

    private List<ProductDTO> productList;

    @Captor
    private ArgumentCaptor<BMICategory> bmiCategoryArgumentCaptor;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void init() {
        productList = List.of(
                new ProductDTO(1L, "bread", new BigDecimal(15), "desc",
                        120, 24, 4.2, 4, 158.85, 0.6, 2.0, 2.7,
                        4.2, "A", "OVERWEIGHT", null, null, null,
                        null, null),
                new ProductDTO(1L, "beer", new BigDecimal(15), "desc",
                        120, 24, 4.2, 4, 158.85, 0.6, 2.0, 2.7,
                        4.2, "A", "OVERWEIGHT", null, null, null,
                        null, null),
                new ProductDTO(1L, "meat", new BigDecimal(15), "desc",
                        120, 24, 4.2, 4, 158.85, 0.6, 2.0, 2.7,
                        4.2, "A", "OVERWEIGHT", null, null, null,
                        null, null),
                new ProductDTO(1L, "potato", new BigDecimal(15), "desc",
                        120, 24, 4.2, 4, 158.85, 0.6, 2.0, 2.7,
                        4.2, "A", "OVERWEIGHT", null, null, null,
                        null, null),
                new ProductDTO(1L, "milk", new BigDecimal(15), "desc",
                        120, 24, 4.2, 4, 158.85, 0.6, 2.0, 2.7,
                        4.2, "A", "OVERWEIGHT", null, null, null,
                        null, null),
                new ProductDTO(1L, "tea", new BigDecimal(15), "desc",
                        120, 24, 4.2, 4, 158.85, 0.6, 2.0, 2.7,
                        4.2, "A", "OVERWEIGHT", null, null, null,
                        null, null),
                new ProductDTO(1L, "wine", new BigDecimal(15), "desc",
                        120, 24, 4.2, 4, 158.85, 0.6, 2.0, 2.7,
                        4.2, "A", "OVERWEIGHT", null, null, null,
                        null, null)
        );
    }


    @DisplayName("wrong age")
    @Nested
    class WrongAgeTest {

        @ParameterizedTest
        @MethodSource("minWrongAgeProvider")
        @DisplayName("validation failed when wrong min age")
        void shouldDontPassValidation_when_wrongMinAge(int wrongAge) {
            BmiRequest bmiRequest = new BmiRequest(wrongAge, Gender.FEMALE, 25, 158);
            Set<ConstraintViolation<BmiRequest>> violations = validator.validate(bmiRequest);
            assertNotNull(violations);
            assertEquals(BmiConstraints.MIN_AGE_EXCEPTION_MESSAGE, violations.iterator().next().getMessage());
            verifyNoInteractions(groqCaller, productService);
        }


        @ParameterizedTest
        @MethodSource("maxWrongAgeProvider")
        @DisplayName("validation failed when wrong max age")
        void shouldDontPassValidation_when_wrongMaxAge(int wrongAge) {
            BmiRequest bmiRequest = new BmiRequest(wrongAge, Gender.FEMALE, 250, 158);
            Set<ConstraintViolation<BmiRequest>> violations = validator.validate(bmiRequest);
            assertNotNull(violations);
            assertEquals(BmiConstraints.MAX_AGE_EXCEPTION_MESSAGE, violations.iterator().next().getMessage());
            verifyNoInteractions(groqCaller, productService);
        }


        private static Stream<Arguments> minWrongAgeProvider() {
            return Stream.of(
                    Arguments.of(0),
                    Arguments.of(-1),
                    Arguments.of(1),
                    Arguments.of(-8000),
                    Arguments.of(-562),
                    Arguments.of(-0),
                    Arguments.of(Integer.MIN_VALUE)
            );
        }

        private static Stream<Arguments> maxWrongAgeProvider() {
            return Stream.of(
                    Arguments.of(121),
                    Arguments.of(18000),
                    Arguments.of(562),
                    Arguments.of(Integer.MAX_VALUE)
            );
        }
    }

    @DisplayName("wrong weightKg")
    @Nested
    class WrongWeightKgTest {
        @ParameterizedTest
        @MethodSource("minWrongWeightKgProvider")
        @DisplayName("validation failed when wrong min weightKg")
        void shouldDontPassValidation_when_wrongMinWeightKg(double wrongWeightKg) {
            BmiRequest bmiRequest = new BmiRequest(18, Gender.FEMALE, wrongWeightKg, 158);
            Set<ConstraintViolation<BmiRequest>> violations = validator.validate(bmiRequest);
            assertNotNull(violations);
            assertEquals(BmiConstraints.MIN_WEIGHT_EXCEPTION_MESSAGE, violations.iterator().next().getMessage());
            verifyNoInteractions(groqCaller, productService);
        }

        @ParameterizedTest
        @MethodSource("maxWrongWeightKgProvider")
        @DisplayName("validation failed when wrong max weightKg")
        void shouldDontPassValidation_when_wrongMaxWeightKg(double wrongWeightKg) {
            BmiRequest bmiRequest = new BmiRequest(18, Gender.FEMALE, wrongWeightKg, 180);
            Set<ConstraintViolation<BmiRequest>> violations = validator.validate(bmiRequest);
            assertNotNull(violations);
            assertEquals(BmiConstraints.MAX_WEIGHT_EXCEPTION_MESSAGE, violations.iterator().next().getMessage());
            verifyNoInteractions(groqCaller, productService);
        }

        private static Stream<Arguments> minWrongWeightKgProvider() {
            return Stream.of(
                    Arguments.of(1),
                    Arguments.of(0),
                    Arguments.of(-1),
                    Arguments.of(-125),
                    Arguments.of(-864123),
                    Arguments.of(Integer.MIN_VALUE)
            );
        }

        private static Stream<Arguments> maxWrongWeightKgProvider() {
            return Stream.of(
                    Arguments.of(251),
                    Arguments.of(500),
                    Arguments.of(272),
                    Arguments.of(8232156),
                    Arguments.of(485),
                    Arguments.of(Integer.MAX_VALUE)
            );
        }
    }


    @DisplayName("wrong heightCm")
    @Nested
    class WrongHeightCmTest {
        @ParameterizedTest
        @MethodSource("minWrongHeightCmProvider")
        @DisplayName("validation failed when wrong min heightCm")
        void shouldDontPassValidation_when_wrongWeightKg(double wrongHeightCm) {
            BmiRequest bmiRequest = new BmiRequest(18, Gender.FEMALE, 75, wrongHeightCm);
            Set<ConstraintViolation<BmiRequest>> violations = validator.validate(bmiRequest);
            assertNotNull(violations);
            assertEquals(BmiConstraints.MIN_HEIGHT_EXCEPTION_MESSAGE, violations.iterator().next().getMessage());
            verifyNoInteractions(groqCaller, productService);
        }

        @ParameterizedTest
        @MethodSource("maxWrongHeightCmProvider")
        @DisplayName("validation failed when wrong max heightCm")
        void shouldDontPassValidation_when_wrongMaxWeightKg(double wrongHeightCm) {
            BmiRequest bmiRequest = new BmiRequest(18, Gender.FEMALE, 75, wrongHeightCm);
            Set<ConstraintViolation<BmiRequest>> violations = validator.validate(bmiRequest);
            assertNotNull(violations);
            assertEquals(BmiConstraints.MAX_HEIGHT_EXCEPTION_MESSAGE, violations.iterator().next().getMessage());
            verifyNoInteractions(groqCaller, productService);
        }

        private static Stream<Arguments> minWrongHeightCmProvider() {
            return Stream.of(
                    Arguments.of(1),
                    Arguments.of(0),
                    Arguments.of(20),
                    Arguments.of(24),
                    Arguments.of(15),
                    Arguments.of(-1),
                    Arguments.of(-125),
                    Arguments.of(-864123),
                    Arguments.of(Integer.MIN_VALUE)
            );
        }

        private static Stream<Arguments> maxWrongHeightCmProvider() {
            return Stream.of(
                    Arguments.of(251),
                    Arguments.of(500),
                    Arguments.of(272),
                    Arguments.of(8232156),
                    Arguments.of(485),
                    Arguments.of(Integer.MAX_VALUE)
            );
        }
    }

    @DisplayName("get overweight bmi category for teen")
    @Nested
    class GetOverweightBmiCategoryForTeen {

        @ParameterizedTest
        @MethodSource("overWeightKgForTeenGirlProvider")
        @DisplayName("get overweight for teen girl")
        void shouldReturnOverweightCategory_when_teenGirlIsOverweight(Double weightKg) {
            BmiRequest bmiRequest = new BmiRequest(18, Gender.FEMALE, weightKg, 167);
            doReturn(productList).when(productService).getProductsByBMICategory(any());
            doReturn("OVERWEIGHT").when(groqCaller).callPrompt(any());

            BmiResponse bmiResponse = bmiService.calculateBmi(bmiRequest);

            verify(productService).getProductsByBMICategory(bmiCategoryArgumentCaptor.capture());
            assertNotNull(bmiResponse);
            assertEquals(BMICategory.OVERWEIGHT, bmiCategoryArgumentCaptor.getValue());
            assertEquals(BMICategory.OVERWEIGHT.getCategory(), bmiResponse.category());
            assertEquals(HealthRiskEsteemRecommendations.FOR_OVERWEIGHT, bmiResponse.healthRisk());
            assertNotNull(bmiResponse.items());
            assertEquals(5, bmiResponse.items().size());
            assertEquals("OVERWEIGHT", bmiResponse.aiResponse());

            verify(productService, times(1)).getProductsByBMICategory(any());
            verify(groqCaller, times(1)).callPrompt(any());
            verifyNoMoreInteractions(productService, groqCaller);
        }

        @ParameterizedTest
        @MethodSource("overWeightKgForTeenBoyProvider")
        @DisplayName("get overweight for teen boy")
        void shouldReturnOverweightCategory_when_teenBoyIsOverweight(Double weightKg) {
            BmiRequest bmiRequest = new BmiRequest(18, Gender.MALE, weightKg, 173);
            doReturn(productList).when(productService).getProductsByBMICategory(any());
            doReturn("OVERWEIGHT").when(groqCaller).callPrompt(any());

            BmiResponse bmiResponse = bmiService.calculateBmi(bmiRequest);

            verify(productService).getProductsByBMICategory(bmiCategoryArgumentCaptor.capture());
            assertNotNull(bmiResponse);
            assertEquals(BMICategory.OVERWEIGHT, bmiCategoryArgumentCaptor.getValue());
            assertEquals(BMICategory.OVERWEIGHT.getCategory(), bmiResponse.category());
            assertEquals(HealthRiskEsteemRecommendations.FOR_OVERWEIGHT, bmiResponse.healthRisk());
            assertNotNull(bmiResponse.items());
            assertEquals(5, bmiResponse.items().size());
            assertEquals("OVERWEIGHT", bmiResponse.aiResponse());

            verify(productService, times(1)).getProductsByBMICategory(any());
            verify(groqCaller, times(1)).callPrompt(any());
            verifyNoMoreInteractions(productService, groqCaller);
        }


        private static Stream<Arguments> overWeightKgForTeenGirlProvider() {
            return Stream.of(
                    Arguments.of(62.77),
                    Arguments.of(62.8),
                    Arguments.of(62.9),
                    Arguments.of(63.0),
                    Arguments.of(63.5),
                    Arguments.of(Double.MAX_VALUE)
            );
        }

        private static Stream<Arguments> overWeightKgForTeenBoyProvider() {
            return Stream.of(
                    Arguments.of(68.88),
                    Arguments.of(69.0),
                    Arguments.of(69.5),
                    Arguments.of(70.0),
                    Arguments.of(75.0),
                    Arguments.of(80.0),
                    Arguments.of(81.0),
                    Arguments.of(Double.MAX_VALUE)
            );
        }
    }

    @DisplayName("get normal bmi category for teen")
    @Nested
    class GetNormalBmiCategoryForTeen {

        @ParameterizedTest
        @MethodSource("normalWeightForTeenGirlProvider")
        @DisplayName("get normal weight for teen girl")
        void shouldReturnNormalWeightCategory_when_teenGirlIsNormalWeight(Double weightKg) {
            BmiRequest bmiRequest = new BmiRequest(18, Gender.FEMALE, weightKg, 167);
            doReturn(productList).when(productService).getProductsByBMICategory(any());
            doReturn(BMICategory.NORMAL_WEIGHT.toString()).when(groqCaller).callPrompt(any());

            BmiResponse bmiResponse = bmiService.calculateBmi(bmiRequest);

            verify(productService).getProductsByBMICategory(bmiCategoryArgumentCaptor.capture());
            assertNotNull(bmiResponse);
            assertEquals(BMICategory.NORMAL_WEIGHT, bmiCategoryArgumentCaptor.getValue());
            assertEquals(BMICategory.NORMAL_WEIGHT.getCategory(), bmiResponse.category());
            assertEquals(HealthRiskEsteemRecommendations.FOR_NORMAL_WEIGHT, bmiResponse.healthRisk());
            assertNotNull(bmiResponse.items());
            assertEquals(5, bmiResponse.items().size());
            assertEquals(BMICategory.NORMAL_WEIGHT.toString(), bmiResponse.aiResponse());

            verify(productService, times(1)).getProductsByBMICategory(any());
            verify(groqCaller, times(1)).callPrompt(any());
            verifyNoMoreInteractions(productService, groqCaller);
        }

        @ParameterizedTest
        @MethodSource("normalWeightForTeenBoyProvider")
        @DisplayName("get normal weight for teen boy")
        void shouldReturnNormalWeightCategory_when_teenBoyIsNormalWeight(Double weightKg) {
            BmiRequest bmiRequest = new BmiRequest(18, Gender.MALE, weightKg, 173);
            doReturn(productList).when(productService).getProductsByBMICategory(any());
            doReturn(BMICategory.NORMAL_WEIGHT.toString()).when(groqCaller).callPrompt(any());

            BmiResponse bmiResponse = bmiService.calculateBmi(bmiRequest);

            verify(productService).getProductsByBMICategory(bmiCategoryArgumentCaptor.capture());
            assertNotNull(bmiResponse);
            assertEquals(BMICategory.NORMAL_WEIGHT, bmiCategoryArgumentCaptor.getValue());
            assertEquals(BMICategory.NORMAL_WEIGHT.getCategory(), bmiResponse.category());
            assertEquals(HealthRiskEsteemRecommendations.FOR_NORMAL_WEIGHT, bmiResponse.healthRisk());
            assertNotNull(bmiResponse.items());
            assertEquals(5, bmiResponse.items().size());
            assertEquals(BMICategory.NORMAL_WEIGHT.toString(), bmiResponse.aiResponse());

            verify(productService, times(1)).getProductsByBMICategory(any());
            verify(groqCaller, times(1)).callPrompt(any());
            verifyNoMoreInteractions(productService, groqCaller);
        }


        private static Stream<Arguments> normalWeightForTeenGirlProvider() {
            return Stream.of(
                    Arguments.of(47.4),
                    Arguments.of(48.0),
                    Arguments.of(53.45),
                    Arguments.of(59.12),
                    Arguments.of(62.7)
            );
        }

        private static Stream<Arguments> normalWeightForTeenBoyProvider() {
            return Stream.of(
                    Arguments.of(52.4),
                    Arguments.of(52.5),
                    Arguments.of(53.5),
                    Arguments.of(54.0),
                    Arguments.of(58.8),
                    Arguments.of(60.2),
                    Arguments.of(64.5),
                    Arguments.of(66.0),
                    Arguments.of(68.8)
            );
        }
    }

    @DisplayName("get under bmi category for teen")
    @Nested
    class GetUnderBmiCategoryForTeen {

        @ParameterizedTest
        @MethodSource("underWeightForTeenGirlProvider")
        @DisplayName("get underWeight for teen girl")
        void shouldReturnUnderWeightCategory_when_teenGirlIsUnderWeight(Double weightKg) {
            BmiRequest bmiRequest = new BmiRequest(18, Gender.FEMALE, weightKg, 167);
            doReturn(productList).when(productService).getProductsByBMICategory(any());
            doReturn(BMICategory.UNDERWEIGHT.toString()).when(groqCaller).callPrompt(any());

            BmiResponse bmiResponse = bmiService.calculateBmi(bmiRequest);

            verify(productService).getProductsByBMICategory(bmiCategoryArgumentCaptor.capture());
            assertNotNull(bmiResponse);
            assertEquals(BMICategory.UNDERWEIGHT, bmiCategoryArgumentCaptor.getValue());
            assertEquals(BMICategory.UNDERWEIGHT.getCategory(), bmiResponse.category());
            assertEquals(HealthRiskEsteemRecommendations.FOR_UNDERWEIGHT, bmiResponse.healthRisk());
            assertNotNull(bmiResponse.items());
            assertEquals(5, bmiResponse.items().size());
            assertEquals(BMICategory.UNDERWEIGHT.toString(), bmiResponse.aiResponse());

            verify(productService, times(1)).getProductsByBMICategory(any());
            verify(groqCaller, times(1)).callPrompt(any());
            verifyNoMoreInteractions(productService, groqCaller);
        }

        @ParameterizedTest
        @MethodSource("underWeightForTeenBoyProvider")
        @DisplayName("get underWeight for teen boy")
        void shouldReturnUnderWeightCategory_when_teenBoyIsUnderWeight(Double weightKg) {
            BmiRequest bmiRequest = new BmiRequest(18, Gender.MALE, weightKg, 173);
            doReturn(productList).when(productService).getProductsByBMICategory(any());
            doReturn(BMICategory.UNDERWEIGHT.toString()).when(groqCaller).callPrompt(any());

            BmiResponse bmiResponse = bmiService.calculateBmi(bmiRequest);

            verify(productService).getProductsByBMICategory(bmiCategoryArgumentCaptor.capture());
            assertNotNull(bmiResponse);
            assertEquals(BMICategory.UNDERWEIGHT, bmiCategoryArgumentCaptor.getValue());
            assertEquals(BMICategory.UNDERWEIGHT.getCategory(), bmiResponse.category());
            assertEquals(HealthRiskEsteemRecommendations.FOR_UNDERWEIGHT, bmiResponse.healthRisk());
            assertNotNull(bmiResponse.items());
            assertEquals(5, bmiResponse.items().size());
            assertEquals(BMICategory.UNDERWEIGHT.toString(), bmiResponse.aiResponse());

            verify(productService, times(1)).getProductsByBMICategory(any());
            verify(groqCaller, times(1)).callPrompt(any());
            verifyNoMoreInteractions(productService, groqCaller);
        }

        private static Stream<Arguments> underWeightForTeenGirlProvider() {
            return Stream.of(
                    Arguments.of(12.2),
                    Arguments.of(15.0),
                    Arguments.of(20.0),
                    Arguments.of(35.87),
                    Arguments.of(42.61),
                    Arguments.of(47.39)
            );
        }

        private static Stream<Arguments> underWeightForTeenBoyProvider() {
            return Stream.of(
                    Arguments.of(12.2),
                    Arguments.of(20.0),
                    Arguments.of(31.12),
                    Arguments.of(38.15),
                    Arguments.of(44.0),
                    Arguments.of(49.75),
                    Arguments.of(50.0),
                    Arguments.of(51.5),
                    Arguments.of(52.3)
            );
        }
    }

    @DisplayName("get overweight bmi category for adult")
    @Nested
    class GetOverweightBmiCategoryForAdult {

        @ParameterizedTest
        @MethodSource("overWeightKgForWomanProvider")
        @DisplayName("get overweight for woman")
        void shouldReturnOverweightCategory_when_womanIsOverweight(Double weightKg) {
            BmiRequest bmiRequest = new BmiRequest(28, Gender.FEMALE, weightKg, 171);
            doReturn(productList).when(productService).getProductsByBMICategory(any());
            doReturn(BMICategory.OVERWEIGHT.toString()).when(groqCaller).callPrompt(any());

            BmiResponse bmiResponse = bmiService.calculateBmi(bmiRequest);

            verify(productService).getProductsByBMICategory(bmiCategoryArgumentCaptor.capture());
            assertNotNull(bmiResponse);
            assertEquals(BMICategory.OVERWEIGHT, bmiCategoryArgumentCaptor.getValue());
            assertEquals(BMICategory.OVERWEIGHT.getCategory(), bmiResponse.category());
            assertEquals(HealthRiskEsteemRecommendations.FOR_OVERWEIGHT, bmiResponse.healthRisk());
            assertNotNull(bmiResponse.items());
            assertEquals(5, bmiResponse.items().size());
            assertEquals(BMICategory.OVERWEIGHT.toString(), bmiResponse.aiResponse());

            verify(productService, times(1)).getProductsByBMICategory(any());
            verify(groqCaller, times(1)).callPrompt(any());
            verifyNoMoreInteractions(productService, groqCaller);
        }

        @ParameterizedTest
        @MethodSource("overWeightKgForManProvider")
        @DisplayName("get overweight for man")
        void shouldReturnOverweightCategory_when_ManIsOverweight(Double weightKg) {
            BmiRequest bmiRequest = new BmiRequest(18, Gender.MALE, weightKg, 178);
            doReturn(productList).when(productService).getProductsByBMICategory(any());
            doReturn(BMICategory.OVERWEIGHT.toString()).when(groqCaller).callPrompt(any());

            BmiResponse bmiResponse = bmiService.calculateBmi(bmiRequest);

            verify(productService).getProductsByBMICategory(bmiCategoryArgumentCaptor.capture());
            assertNotNull(bmiResponse);
            assertEquals(BMICategory.OVERWEIGHT, bmiCategoryArgumentCaptor.getValue());
            assertEquals(BMICategory.OVERWEIGHT.getCategory(), bmiResponse.category());
            assertEquals(HealthRiskEsteemRecommendations.FOR_OVERWEIGHT, bmiResponse.healthRisk());
            assertNotNull(bmiResponse.items());
            assertEquals(5, bmiResponse.items().size());
            assertEquals(BMICategory.OVERWEIGHT.toString(), bmiResponse.aiResponse());

            verify(productService, times(1)).getProductsByBMICategory(any());
            verify(groqCaller, times(1)).callPrompt(any());
            verifyNoMoreInteractions(productService, groqCaller);
        }


        private static Stream<Arguments> overWeightKgForWomanProvider() {
            return Stream.of(
                    Arguments.of(73.1),
                    Arguments.of(73.2),
                    Arguments.of(75.0),
                    Arguments.of(82.4),
                    Arguments.of(90.2),
                    Arguments.of(115.4),
                    Arguments.of(Double.MAX_VALUE)
            );
        }

        private static Stream<Arguments> overWeightKgForManProvider() {
            return Stream.of(
                    Arguments.of(79.2),
                    Arguments.of(79.3),
                    Arguments.of(80.0),
                    Arguments.of(84.6),
                    Arguments.of(91.8),
                    Arguments.of(98.2),
                    Arguments.of(112.12),
                    Arguments.of(Double.MAX_VALUE)
            );
        }
    }

    @DisplayName("get normal bmi category for adult")
    @Nested
    class GetNormalWeightBmiCategoryForAdult {

        @ParameterizedTest
        @MethodSource("normalWeightKgForWomanProvider")
        @DisplayName("get normalWeight for woman")
        void shouldReturnNormalWeightCategory_when_womanIsNormalWeight(Double weightKg) {
            BmiRequest bmiRequest = new BmiRequest(28, Gender.FEMALE, weightKg, 171);
            doReturn(productList).when(productService).getProductsByBMICategory(any());
            doReturn(BMICategory.NORMAL_WEIGHT.toString()).when(groqCaller).callPrompt(any());

            BmiResponse bmiResponse = bmiService.calculateBmi(bmiRequest);

            verify(productService).getProductsByBMICategory(bmiCategoryArgumentCaptor.capture());
            assertNotNull(bmiResponse);
            assertEquals(BMICategory.NORMAL_WEIGHT, bmiCategoryArgumentCaptor.getValue());
            assertEquals(BMICategory.NORMAL_WEIGHT.getCategory(), bmiResponse.category());
            assertEquals(HealthRiskEsteemRecommendations.FOR_NORMAL_WEIGHT, bmiResponse.healthRisk());
            assertNotNull(bmiResponse.items());
            assertEquals(5, bmiResponse.items().size());
            assertEquals(BMICategory.NORMAL_WEIGHT.toString(), bmiResponse.aiResponse());

            verify(productService, times(1)).getProductsByBMICategory(any());
            verify(groqCaller, times(1)).callPrompt(any());
            verifyNoMoreInteractions(productService, groqCaller);
        }

        @ParameterizedTest
        @MethodSource("normalWeightKgForManProvider")
        @DisplayName("get normalWeight for man")
        void shouldReturnNormalWeightCategory_when_ManIsNormalWeight(Double weightKg) {
            BmiRequest bmiRequest = new BmiRequest(28, Gender.MALE, weightKg, 178);
            doReturn(productList).when(productService).getProductsByBMICategory(any());
            doReturn(BMICategory.NORMAL_WEIGHT.toString()).when(groqCaller).callPrompt(any());

            BmiResponse bmiResponse = bmiService.calculateBmi(bmiRequest);

            verify(productService).getProductsByBMICategory(bmiCategoryArgumentCaptor.capture());
            assertNotNull(bmiResponse);
            assertEquals(BMICategory.NORMAL_WEIGHT, bmiCategoryArgumentCaptor.getValue());
            assertEquals(BMICategory.NORMAL_WEIGHT.getCategory(), bmiResponse.category());
            assertEquals(HealthRiskEsteemRecommendations.FOR_NORMAL_WEIGHT, bmiResponse.healthRisk());
            assertNotNull(bmiResponse.items());
            assertEquals(5, bmiResponse.items().size());
            assertEquals(BMICategory.NORMAL_WEIGHT.toString(), bmiResponse.aiResponse());

            verify(productService, times(1)).getProductsByBMICategory(any());
            verify(groqCaller, times(1)).callPrompt(any());
            verifyNoMoreInteractions(productService, groqCaller);
        }


        private static Stream<Arguments> normalWeightKgForWomanProvider() {
            return Stream.of(
                    Arguments.of(54.1),
                    Arguments.of(54.5),
                    Arguments.of(59.9),
                    Arguments.of(62.6),
                    Arguments.of(69.45),
                    Arguments.of(70.02),
                    Arguments.of(73.0)
            );
        }

        private static Stream<Arguments> normalWeightKgForManProvider() {
            return Stream.of(
                    Arguments.of(58.6),
                    Arguments.of(59.0),
                    Arguments.of(64.2),
                    Arguments.of(67.26),
                    Arguments.of(70.39),
                    Arguments.of(74.93),
                    Arguments.of(75.0),
                    Arguments.of(79.19)
            );
        }
    }

    @DisplayName("get underweight bmi category for adult")
    @Nested
    class GetUnderWeightBmiCategoryForAdult {

        @ParameterizedTest
        @MethodSource("underWightKgForWomanProvider")
        @DisplayName("get underWeight for woman")
        void shouldReturnUNderWeightCategory_when_womanIsUnderWeight(Double weightKg) {
            BmiRequest bmiRequest = new BmiRequest(28, Gender.FEMALE, weightKg, 171);
            doReturn(productList).when(productService).getProductsByBMICategory(any());
            doReturn(BMICategory.UNDERWEIGHT.toString()).when(groqCaller).callPrompt(any());

            BmiResponse bmiResponse = bmiService.calculateBmi(bmiRequest);

            verify(productService).getProductsByBMICategory(bmiCategoryArgumentCaptor.capture());
            assertNotNull(bmiResponse);
            assertEquals(BMICategory.UNDERWEIGHT, bmiCategoryArgumentCaptor.getValue());
            assertEquals(BMICategory.UNDERWEIGHT.getCategory(), bmiResponse.category());
            assertEquals(HealthRiskEsteemRecommendations.FOR_UNDERWEIGHT, bmiResponse.healthRisk());
            assertNotNull(bmiResponse.items());
            assertEquals(5, bmiResponse.items().size());
            assertEquals(BMICategory.UNDERWEIGHT.toString(), bmiResponse.aiResponse());

            verify(productService, times(1)).getProductsByBMICategory(any());
            verify(groqCaller, times(1)).callPrompt(any());
            verifyNoMoreInteractions(productService, groqCaller);
        }

        @ParameterizedTest
        @MethodSource("underWightKgForManProvider")
        @DisplayName("get underWeight for man")
        void shouldReturnUnderWeightCategory_when_ManIsUnderWeight(Double weightKg) {
            BmiRequest bmiRequest = new BmiRequest(28, Gender.MALE, weightKg, 178);
            doReturn(productList).when(productService).getProductsByBMICategory(any());
            doReturn(BMICategory.UNDERWEIGHT.toString()).when(groqCaller).callPrompt(any());

            BmiResponse bmiResponse = bmiService.calculateBmi(bmiRequest);

            verify(productService).getProductsByBMICategory(bmiCategoryArgumentCaptor.capture());
            assertNotNull(bmiResponse);
            assertEquals(BMICategory.UNDERWEIGHT, bmiCategoryArgumentCaptor.getValue());
            assertEquals(BMICategory.UNDERWEIGHT.getCategory(), bmiResponse.category());
            assertEquals(HealthRiskEsteemRecommendations.FOR_UNDERWEIGHT, bmiResponse.healthRisk());
            assertNotNull(bmiResponse.items());
            assertEquals(5, bmiResponse.items().size());
            assertEquals(BMICategory.UNDERWEIGHT.toString(), bmiResponse.aiResponse());

            verify(productService, times(1)).getProductsByBMICategory(any());
            verify(groqCaller, times(1)).callPrompt(any());
            verifyNoMoreInteractions(productService, groqCaller);
        }

        private static Stream<Arguments> underWightKgForWomanProvider() {
            return Stream.of(
                    Arguments.of(5.0),
                    Arguments.of(18.0),
                    Arguments.of(24.12),
                    Arguments.of(36.87),
                    Arguments.of(44.96),
                    Arguments.of(54.0)
            );
        }

        private static Stream<Arguments> underWightKgForManProvider() {
            return Stream.of(
                    Arguments.of(4.5),
                    Arguments.of(15.15),
                    Arguments.of(29.6),
                    Arguments.of(34.6),
                    Arguments.of(49.1),
                    Arguments.of(54.32),
                    Arguments.of(58.59)
            );
        }
    }


    @DisplayName("get overweight bmi category for senior")
    @Nested
    class GetOverweightBmiCategoryForSenior {

        @ParameterizedTest
        @MethodSource("overWeightKgForWomanProvider")
        @DisplayName("get overweight for woman")
        void shouldReturnOverweightCategory_when_womanIsOverweight(Double weightKg) {
            BmiRequest bmiRequest = new BmiRequest(61, Gender.FEMALE, weightKg, 171);
            doReturn(productList).when(productService).getProductsByBMICategory(any());
            doReturn(BMICategory.OVERWEIGHT.toString()).when(groqCaller).callPrompt(any());

            BmiResponse bmiResponse = bmiService.calculateBmi(bmiRequest);

            verify(productService).getProductsByBMICategory(bmiCategoryArgumentCaptor.capture());
            assertNotNull(bmiResponse);
            assertEquals(BMICategory.OVERWEIGHT, bmiCategoryArgumentCaptor.getValue());
            assertEquals(BMICategory.OVERWEIGHT.getCategory(), bmiResponse.category());
            assertEquals(HealthRiskEsteemRecommendations.FOR_OVERWEIGHT, bmiResponse.healthRisk());
            assertNotNull(bmiResponse.items());
            assertEquals(5, bmiResponse.items().size());
            assertEquals(BMICategory.OVERWEIGHT.toString(), bmiResponse.aiResponse());

            verify(productService, times(1)).getProductsByBMICategory(any());
            verify(groqCaller, times(1)).callPrompt(any());
            verifyNoMoreInteractions(productService, groqCaller);
        }

        @ParameterizedTest
        @MethodSource("overWeightKgForManProvider")
        @DisplayName("get overweight for man")
        void shouldReturnOverweightCategory_when_ManIsOverweight(Double weightKg) {
            BmiRequest bmiRequest = new BmiRequest(18, Gender.MALE, weightKg, 178);
            doReturn(productList).when(productService).getProductsByBMICategory(any());
            doReturn(BMICategory.OVERWEIGHT.toString()).when(groqCaller).callPrompt(any());

            BmiResponse bmiResponse = bmiService.calculateBmi(bmiRequest);

            verify(productService).getProductsByBMICategory(bmiCategoryArgumentCaptor.capture());
            assertNotNull(bmiResponse);
            assertEquals(BMICategory.OVERWEIGHT, bmiCategoryArgumentCaptor.getValue());
            assertEquals(BMICategory.OVERWEIGHT.getCategory(), bmiResponse.category());
            assertEquals(HealthRiskEsteemRecommendations.FOR_OVERWEIGHT, bmiResponse.healthRisk());
            assertNotNull(bmiResponse.items());
            assertEquals(5, bmiResponse.items().size());
            assertEquals(BMICategory.OVERWEIGHT.toString(), bmiResponse.aiResponse());

            verify(productService, times(1)).getProductsByBMICategory(any());
            verify(groqCaller, times(1)).callPrompt(any());
            verifyNoMoreInteractions(productService, groqCaller);
        }


        private static Stream<Arguments> overWeightKgForWomanProvider() {
            return Stream.of(
                    Arguments.of(79.0),
                    Arguments.of(79.1),
                    Arguments.of(84.9),
                    Arguments.of(96.8),
                    Arguments.of(114.3),
                    Arguments.of(Double.MAX_VALUE)
            );
        }

        private static Stream<Arguments> overWeightKgForManProvider() {
            return Stream.of(
                    Arguments.of(85.6),
                    Arguments.of(87.8),
                    Arguments.of(98.5),
                    Arguments.of(102.0),
                    Arguments.of(110.3),
                    Arguments.of(Double.MAX_VALUE)
            );
        }
    }


    @DisplayName("get normal bmi category for senior")
    @Nested
    class GetNormalWeightBmiCategoryForSenior {

        @ParameterizedTest
        @MethodSource("normalWeightKgForWomanProvider")
        @DisplayName("get normalWeight for woman")
        void shouldReturnNormalWeightCategory_when_womanIsNormalWeight(Double weightKg) {
            BmiRequest bmiRequest = new BmiRequest(61, Gender.FEMALE, weightKg, 171);
            doReturn(productList).when(productService).getProductsByBMICategory(any());
            doReturn(BMICategory.NORMAL_WEIGHT.toString()).when(groqCaller).callPrompt(any());

            BmiResponse bmiResponse = bmiService.calculateBmi(bmiRequest);

            verify(productService).getProductsByBMICategory(bmiCategoryArgumentCaptor.capture());
            assertNotNull(bmiResponse);
            assertEquals(BMICategory.NORMAL_WEIGHT, bmiCategoryArgumentCaptor.getValue());
            assertEquals(BMICategory.NORMAL_WEIGHT.getCategory(), bmiResponse.category());
            assertEquals(HealthRiskEsteemRecommendations.FOR_NORMAL_WEIGHT, bmiResponse.healthRisk());
            assertNotNull(bmiResponse.items());
            assertEquals(5, bmiResponse.items().size());
            assertEquals(BMICategory.NORMAL_WEIGHT.toString(), bmiResponse.aiResponse());

            verify(productService, times(1)).getProductsByBMICategory(any());
            verify(groqCaller, times(1)).callPrompt(any());
            verifyNoMoreInteractions(productService, groqCaller);
        }

        @ParameterizedTest
        @MethodSource("normalWeightKgForManProvider")
        @DisplayName("get normalWeight for man")
        void shouldReturnNormalWeightCategory_when_ManIsNormalWeight(Double weightKg) {
            BmiRequest bmiRequest = new BmiRequest(61, Gender.MALE, weightKg, 178);
            doReturn(productList).when(productService).getProductsByBMICategory(any());
            doReturn(BMICategory.NORMAL_WEIGHT.toString()).when(groqCaller).callPrompt(any());

            BmiResponse bmiResponse = bmiService.calculateBmi(bmiRequest);

            verify(productService).getProductsByBMICategory(bmiCategoryArgumentCaptor.capture());
            assertNotNull(bmiResponse);
            assertEquals(BMICategory.NORMAL_WEIGHT, bmiCategoryArgumentCaptor.getValue());
            assertEquals(BMICategory.NORMAL_WEIGHT.getCategory(), bmiResponse.category());
            assertEquals(HealthRiskEsteemRecommendations.FOR_NORMAL_WEIGHT, bmiResponse.healthRisk());
            assertNotNull(bmiResponse.items());
            assertEquals(5, bmiResponse.items().size());
            assertEquals(BMICategory.NORMAL_WEIGHT.toString(), bmiResponse.aiResponse());

            verify(productService, times(1)).getProductsByBMICategory(any());
            verify(groqCaller, times(1)).callPrompt(any());
            verifyNoMoreInteractions(productService, groqCaller);
        }


        private static Stream<Arguments> normalWeightKgForWomanProvider() {
            return Stream.of(
                    Arguments.of(64.4),
                    Arguments.of(65.6),
                    Arguments.of(69.3),
                    Arguments.of(70.8),
                    Arguments.of(74.1),
                    Arguments.of(78.9)
            );
        }

        private static Stream<Arguments> normalWeightKgForManProvider() {
            return Stream.of(
                    Arguments.of(69.7),
                    Arguments.of(71.3),
                    Arguments.of(76.2),
                    Arguments.of(80.7),
                    Arguments.of(81.6),
                    Arguments.of(83.6),
                    Arguments.of(85.5)
            );
        }
    }

    @DisplayName("get underweight bmi category for senior")
    @Nested
    class GetUnderWeightBmiCategoryForSenior {

        @ParameterizedTest
        @MethodSource("underWeightKgForWomanProvider")
        @DisplayName("get underWeight for woman")
        void shouldReturnUnderWeightCategory_when_womanIsUnderWeight(Double weightKg) {
            BmiRequest bmiRequest = new BmiRequest(61, Gender.FEMALE, weightKg, 171);
            doReturn(productList).when(productService).getProductsByBMICategory(any());
            doReturn(BMICategory.UNDERWEIGHT.toString()).when(groqCaller).callPrompt(any());

            BmiResponse bmiResponse = bmiService.calculateBmi(bmiRequest);

            verify(productService).getProductsByBMICategory(bmiCategoryArgumentCaptor.capture());
            assertNotNull(bmiResponse);
            assertEquals(BMICategory.UNDERWEIGHT, bmiCategoryArgumentCaptor.getValue());
            assertEquals(BMICategory.UNDERWEIGHT.getCategory(), bmiResponse.category());
            assertEquals(HealthRiskEsteemRecommendations.FOR_UNDERWEIGHT, bmiResponse.healthRisk());
            assertNotNull(bmiResponse.items());
            assertEquals(5, bmiResponse.items().size());
            assertEquals(BMICategory.UNDERWEIGHT.toString(), bmiResponse.aiResponse());

            verify(productService, times(1)).getProductsByBMICategory(any());
            verify(groqCaller, times(1)).callPrompt(any());
            verifyNoMoreInteractions(productService, groqCaller);
        }

        @ParameterizedTest
        @MethodSource("underWeightKgForManProvider")
        @DisplayName("get underWeight for man")
        void shouldReturnUnderWeightCategory_when_ManIsUnderWeight(Double weightKg) {
            BmiRequest bmiRequest = new BmiRequest(61, Gender.MALE, weightKg, 178);
            doReturn(productList).when(productService).getProductsByBMICategory(any());
            doReturn(BMICategory.UNDERWEIGHT.toString()).when(groqCaller).callPrompt(any());

            BmiResponse bmiResponse = bmiService.calculateBmi(bmiRequest);

            verify(productService).getProductsByBMICategory(bmiCategoryArgumentCaptor.capture());
            assertNotNull(bmiResponse);
            assertEquals(BMICategory.UNDERWEIGHT, bmiCategoryArgumentCaptor.getValue());
            assertEquals(BMICategory.UNDERWEIGHT.getCategory(), bmiResponse.category());
            assertEquals(HealthRiskEsteemRecommendations.FOR_UNDERWEIGHT, bmiResponse.healthRisk());
            assertNotNull(bmiResponse.items());
            assertEquals(5, bmiResponse.items().size());
            assertEquals(BMICategory.UNDERWEIGHT.toString(), bmiResponse.aiResponse());

            verify(productService, times(1)).getProductsByBMICategory(any());
            verify(groqCaller, times(1)).callPrompt(any());
            verifyNoMoreInteractions(productService, groqCaller);
        }


        private static Stream<Arguments> underWeightKgForWomanProvider() {
            return Stream.of(
                    Arguments.of(16.2),
                    Arguments.of(31.9),
                    Arguments.of(49.9),
                    Arguments.of(51.2),
                    Arguments.of(59.0),
                    Arguments.of(64.3)
            );
        }

        private static Stream<Arguments> underWeightKgForManProvider() {
            return Stream.of(
                    Arguments.of(10.0),
                    Arguments.of(21.5),
                    Arguments.of(32.2),
                    Arguments.of(48.9),
                    Arguments.of(53.2),
                    Arguments.of(69.6)
            );
        }
    }
}