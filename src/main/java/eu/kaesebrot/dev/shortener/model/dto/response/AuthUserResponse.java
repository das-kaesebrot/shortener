package eu.kaesebrot.dev.shortener.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.kaesebrot.dev.shortener.model.AuthUser;
import eu.kaesebrot.dev.shortener.model.Link;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AuthUserResponse implements Serializable {
    private long version;
    private UUID id;
    private String username;
    private String email;
    @JsonProperty("account_expired_at")
    private Instant accountExpiredAt;
    @JsonProperty("credentials_expired_at")
    private Instant credentialsExpiredAt;
    @JsonProperty("is_locked")
    private boolean locked;
    @JsonProperty("is_enabled")
    private boolean enabled;
    @JsonProperty("links")
    private List<String> linkIds;
    @JsonProperty("created_at")
    private Instant createdAt;
    @JsonProperty("modified_at")
    private Instant modifiedAt;

    public static AuthUserResponse fromAuthUser(AuthUser authUser) {
        return new AuthUserResponse(authUser.getVersion(), authUser.getId(), authUser.getUsername(), authUser.getEmail(), authUser.getAccountExpiredAt(), authUser.getCredentialsExpiredAt(), authUser.isLocked(), authUser.isEnabled(), authUser.getLinks().stream().map(Link::getId).toList(), authUser.getCreatedAt(), authUser.getModifiedAt());
    }
}
