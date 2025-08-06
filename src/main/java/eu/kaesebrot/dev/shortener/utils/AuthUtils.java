package eu.kaesebrot.dev.shortener.utils;

import eu.kaesebrot.dev.shortener.enums.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public final class AuthUtils {
    private AuthUtils() {}

    public static boolean hasScope(Authentication authentication, String scope) {
        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(scope));
    }

    public static HashSet<String> mapScopesFromUserRoleOrdinalRecursively(int ordinal) {
        if (ordinal < UserRole.USER.ordinal()) {
            return new HashSet<>();
        }

        HashSet<String> baseSet = mapScopesFromUserRoleOrdinalRecursively(ordinal - 1);
        baseSet.addAll(mapScopesFromUserRole(UserRole.values()[ordinal]));

        return baseSet;
    }

    public static HashSet<String> mapScopesFromUserRoleRecursively(UserRole role) {
        return mapScopesFromUserRoleOrdinalRecursively(role.ordinal());
    }

    public static HashSet<String> mapScopesFromUserRole(UserRole role) {
        switch (role) {
            case USER -> {
                return new HashSet<>(Set.of("SCOPE_self", "SCOPE_links"));
            }
            case ADMIN -> {
                return new HashSet<>(Set.of("SCOPE_links_admin", "SCOPE_users_read", "SCOPE_users_write", "SCOPE_users_delete"));
            }
            case SUPERADMIN -> {
                return new HashSet<>(Set.of("SCOPE_users_setadmin"));
            }
        }

        return new HashSet<>();
    }

    public static Collection<? extends GrantedAuthority> convertScopesToAuthorities(Set<String> scopes) {
        return scopes.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }
}
