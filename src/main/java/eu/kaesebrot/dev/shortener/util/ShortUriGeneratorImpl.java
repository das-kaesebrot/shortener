package eu.kaesebrot.dev.shortener.util;

import java.security.SecureRandom;
import java.util.HexFormat;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ShortUriGeneratorImpl implements ShortUriGenerator {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final SecureRandom secureRandom = new SecureRandom();

    // this is terrible
    @Override
    public String generate(int length) {
        var seed = secureRandom.generateSeed(secureRandom.nextInt(24, 32));
        var rawKey = HexFormat.of().formatHex(seed);

        var hashedKey = passwordEncoder.encode(rawKey);

        return hashedKey.substring(0, length);
    }
}
