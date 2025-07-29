package eu.kaesebrot.dev.shortener.utils;

public interface RandomStringGenerator {
    public String generate(int length);
    public String generate(int length, String alphabet);
    public String generateHexToken();
    public String generateHexToken(int length);
}
