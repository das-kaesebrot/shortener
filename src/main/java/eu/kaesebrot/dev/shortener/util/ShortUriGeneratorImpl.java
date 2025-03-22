package eu.kaesebrot.dev.shortener.util;

import java.util.UUID;

public class ShortUriGeneratorImpl implements ShortUriGenerator {

    @Override
    public String generate(int length) {
        return UUID.randomUUID().toString().substring(0, length);
    }
}
