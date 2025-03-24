package eu.kaesebrot.dev.shortener.utils;

import java.security.SecureRandom;
import java.util.HexFormat;

import org.springframework.stereotype.Service;

@Service
public class HexStringGeneratorImpl implements HexStringGenerator {
    private final SecureRandom secureRandom = new SecureRandom();
    
    private final int TOKEN_BYTES = 32;

    @Override
    public String generate(int length) {
        // generate bytes in half the amount of the requested length (rounded up) since 2 hex chars are 1 byte
        byte[] seed = secureRandom.generateSeed((int) Math.ceil(length / 2D));
        String hexString = HexFormat.of().formatHex(seed);
        return hexString.substring(0, length);
    }

    @Override
    public String generateToken() {
        // generate 32 Byte -> 64 hex chars
        byte[] seed = secureRandom.generateSeed(TOKEN_BYTES);
        return HexFormat.of().formatHex(seed);
    }
}
