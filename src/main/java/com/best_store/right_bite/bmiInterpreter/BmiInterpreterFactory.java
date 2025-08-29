package com.best_store.right_bite.bmiInterpreter;


import com.best_store.right_bite.constant.bmi.Gender;
import lombok.experimental.UtilityClass;

/**
 *  Chooses appropriate class to analyze BMI.
 *  Uses Factory Design Pattern.
 *  AGE GENDER is required.
 */
@UtilityClass
public class BmiInterpreterFactory {

    /**
     * Chooses appropriate class to analyze BMI.
     * @param gender - gender of person who is calculating BMI
     * @param age - age of person who is calculating BMI
     * @return InterpreterBMI object that is used to analyze BMI.
     */
    public InterpreterBMI getInterpreter(Gender gender, int age) {
        if (age < 20) {
            if (gender == Gender.FEMALE) { // here was written MALE I think it was a mistake I have changed it
                return new TeenGirlBmiInterpreterBMI();
            } else {
                return new TeenBoyBmiInterpreterBMI();
            }
        } else if (age < 60) {
            return new AdultBmiInterpreterBMI();
        } else {
            return new SeniorBmiInterpreterBMI();
        }
    }
}
