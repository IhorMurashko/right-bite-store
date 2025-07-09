package com.best_store.right_bite.util;


import jakarta.annotation.PostConstruct;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Converter
public class CardNumberEncryptor implements AttributeConverter<String, String> {
    @Value("${encryption.secret}")
    private String secretKey;
    private SecretKeySpec keySpec;
    private static final String ALGORITHM = "AES";

    @PostConstruct
    public void init() {
        if (secretKey == null || secretKey.length() != 16) {
            throw new IllegalArgumentException("encryption.secret must be 16 characters long");
        }
        keySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
    }

    @Override
    public String convertToDatabaseColumn(String rawCardNumber) {
        if (rawCardNumber == null) return null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(rawCardNumber.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to encrypt card number", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(dbData));
            return new String(decrypted);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to decrypt card number", e);
        }
    }
}