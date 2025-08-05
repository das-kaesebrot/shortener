package eu.kaesebrot.dev.shortener.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

public class AuthResponseInitial extends AuthResponseBase {
    @NotNull
    @JsonProperty("refresh_token")
    @Getter
    private String refreshToken;

    @NotNull
    @JsonProperty("refresh_token_expires_at")
    @Getter
    private Long refreshTokenExpiresAt;

    public AuthResponseInitial() {
        super();
    }

    public AuthResponseInitial(String jwt, UUID userId, Long expiresAt, String refreshToken, Long refreshTokenExpiresAt) {
        super(jwt, userId, expiresAt);
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresAt = refreshTokenExpiresAt;
    }
}
