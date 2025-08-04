package eu.kaesebrot.dev.shortener.model.dto.response;

public class AuthResponseRefresh extends AuthResponseBase {
    public AuthResponseRefresh(String jwt, String username, Long expiresAt) {
        super(jwt, username, expiresAt);
    }
}
