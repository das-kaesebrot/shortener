package eu.kaesebrot.dev.shortener.utils;

import java.security.SecureRandom;
import java.util.HexFormat;

import org.springframework.stereotype.Service;

@Service
public class RandomStringGeneratorImpl implements RandomStringGenerator {
    private final SecureRandom secureRandom = new SecureRandom();

    private final String RAND_ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private final int TOKEN_BYTES = 32;

    @Override
    public String generate(int length) {
        return generate(length, RAND_ALPHABET);
    }

    @Override
    public String generate(int length, String alphabet) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i ++) {
            stringBuilder.append(alphabet.charAt(secureRandom.nextInt(alphabet.length())));
        }

        return stringBuilder.toString();
    }

    @Override
    public String generateHexToken() {
        // generate 32 Byte -> 64 hex chars
        byte[] seed = secureRandom.generateSeed(TOKEN_BYTES);
        return HexFormat.of().formatHex(seed);
    }
}
