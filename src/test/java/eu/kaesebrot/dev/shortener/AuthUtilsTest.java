package eu.kaesebrot.dev.shortener;


import eu.kaesebrot.dev.shortener.enums.UserRole;
import eu.kaesebrot.dev.shortener.utils.AuthUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles(profiles = "dev")
public class AuthUtilsTest {
    @Test
    @DisplayName("Map correct scopes to user roles")
    void givenUserRole_whenMap_thenReturnCorrectScopes() {
        HashMap<UserRole, Set<String>> expectedScopesPerUserRole = new HashMap<>();
        expectedScopesPerUserRole.put(UserRole.USER, Set.of("SCOPE_self", "SCOPE_links"));
        expectedScopesPerUserRole.put(UserRole.ADMIN, Set.of("SCOPE_self", "SCOPE_links", "SCOPE_links_admin", "SCOPE_users_read", "SCOPE_users_write", "SCOPE_users_delete"));
        expectedScopesPerUserRole.put(UserRole.SUPERADMIN, Set.of("SCOPE_self", "SCOPE_links", "SCOPE_links_admin", "SCOPE_users_read", "SCOPE_users_write", "SCOPE_users_delete", "SCOPE_users_setadmin"));

        for (var item : expectedScopesPerUserRole.entrySet()) {
            HashSet<String> generatedScopes = AuthUtils.mapScopesFromUserRoleRecursively(item.getKey());
            assertEquals(item.getValue(), generatedScopes);
        }
    }
}
