package com.homecloud.api.service;

import java.util.UUID;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

@Service
public class UtilService {

    private Config config;
    private static final String AES_ALGORITHM = "AES/ECB/PKCS5Padding";

    public UtilService(Config config) {
        this.config = config;
    }

    public String getUUIDFromString(String string) {
        return UUID.nameUUIDFromBytes(string.getBytes()).toString();
    }

    public String base64Encode(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }

    public String base64Decode(String input) {
        return new String(Base64.getDecoder().decode(input));
    }

    public String generateUUIDFromEmail(String email) {
        String encodedEmail = encryptString(email);
        return UUID.nameUUIDFromBytes(encodedEmail.getBytes()).toString();
    }

    public String encryptString(String input) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(config.getAesKey().getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
            return base64Encode(encryptedBytes);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to encrypt input string", e);
        }
    }

}
