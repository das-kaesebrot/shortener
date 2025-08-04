package eu.kaesebrot.dev.shortener.utils;

import org.springframework.security.core.Authentication;

public final class AuthUtils {
    private AuthUtils() {}

    public static boolean hasScope(Authentication authentication, String scope) {
        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(scope));
    }
}
