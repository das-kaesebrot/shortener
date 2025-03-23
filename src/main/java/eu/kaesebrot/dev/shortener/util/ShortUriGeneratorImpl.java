package eu.kaesebrot.dev.shortener.util;

import java.security.SecureRandom;
import java.util.HexFormat;

import org.springframework.stereotype.Service;

@Service
public class ShortUriGeneratorImpl implements ShortUriGenerator {
    private final SecureRandom secureRandom = new SecureRandom();

    // this is terrible
    @Override
    public String generate(int length) {
        var seed = secureRandom.generateSeed(secureRandom.nextInt(24, 32));
        var rawKey = HexFormat.of().formatHex(seed);
        return rawKey.substring(0, length);
    }
}
