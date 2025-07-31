package com.best_store.right_bite.constant.bmi;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("Male"),
    FEMALE("Female");
    private String gender;
    Gender(String gender){
        this.gender = gender;
    }
}
