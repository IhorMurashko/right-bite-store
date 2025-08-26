package com.best_store.right_bite.constant.constraint;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BmiConstraints {

    public final int MIN_AGE = 2;
    public final String MIN_AGE_EXCEPTION_MESSAGE = "Age must be greater than 1 year";
    public final int MAX_AGE = 120;
    public final String MAX_AGE_EXCEPTION_MESSAGE = "Age must be less than 120 years";
    public final int MIN_WEIGHT = 2;
    public final String MIN_WEIGHT_EXCEPTION_MESSAGE = "Minimum weight must be greater than 2 kg";
    public final int MAX_WEIGHT = 250;
    public final String MAX_WEIGHT_EXCEPTION_MESSAGE = "Maximum weight must be less than 250 kg";
    public final int MIN_HEIGHT = 25;
    public final String MIN_HEIGHT_EXCEPTION_MESSAGE = "Minimum height must be greater than 24 cm";
    public final int MAX_HEIGHT = 250;
    public final String MAX_HEIGHT_EXCEPTION_MESSAGE = "Maximum height must be less than 250 cm";


}
