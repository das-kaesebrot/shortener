package eu.kaesebrot.dev.shortener.model.dto.response;

import java.util.UUID;

public class AuthResponseRefresh extends AuthResponseBase {
    public AuthResponseRefresh(String jwt, UUID userId, Long expiresAt) {
        super(jwt, userId, expiresAt);
    }
}
