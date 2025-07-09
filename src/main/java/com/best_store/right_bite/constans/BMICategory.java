package com.best_store.right_bite.constans;


import lombok.Getter;

@Getter
public enum BMICategory {
    UNDERWEIGHT("Underweight"),
    NORMAL_WEIGHT("Normal"),
    OVERWEIGHT("Overweight"),
    OBESE("Obese");


    private final String category;

    BMICategory(String category) {
        this.category = category;
    }


}