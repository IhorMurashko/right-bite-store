package com.best_store.right_bite.utils.utilsBMI;

import com.best_store.right_bite.constant.bmi.BMICategory;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;

@Converter(autoApply = true)
public class BMICategoryPostgresConverter  implements AttributeConverter<BMICategory, String> {

    @Override
    public String convertToDatabaseColumn(BMICategory attribute) {
        return attribute != null ? attribute.getCategory() : null;
    }

    @Override
    public BMICategory convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return Arrays.stream(BMICategory.values())
                .filter(b -> b.getCategory().equals(dbData))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown BMICategory: " + dbData));
    }
}
