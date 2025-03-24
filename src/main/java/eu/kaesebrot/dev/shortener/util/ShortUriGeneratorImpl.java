package eu.kaesebrot.dev.shortener.util;

import java.security.SecureRandom;
import java.util.HexFormat;

import org.springframework.stereotype.Service;

@Service
public class ShortUriGeneratorImpl implements ShortUriGenerator {
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String generate(int length) {
        // generate bytes in half the amount of the requested length (rounded up) since 2 hex chars are 1 byte
        byte[] seed = secureRandom.generateSeed((int) Math.ceil(length / 2D));
        String hexString = HexFormat.of().formatHex(seed);
        return hexString.substring(0, length);
    }
}
