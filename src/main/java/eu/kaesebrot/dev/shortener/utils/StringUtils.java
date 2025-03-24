package eu.kaesebrot.dev.shortener.utils;

public final class StringUtils {
    private StringUtils() {}

    public static boolean isNullOrEmpty(String string) {
        return (string == null || string.trim().isEmpty() || string.trim().isBlank());
    }
}
