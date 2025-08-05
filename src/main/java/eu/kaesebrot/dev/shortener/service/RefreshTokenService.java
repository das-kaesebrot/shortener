package eu.kaesebrot.dev.shortener.service;

import eu.kaesebrot.dev.shortener.model.AuthUser;

import java.time.Instant;
import java.util.UUID;

public interface RefreshTokenService {
    String generateRefreshTokenForUser(AuthUser user, Instant expiresAt);
    String generateRefreshTokenForUser(UUID userId, Instant expiresAt);
    boolean isRefreshTokenValid(String rawRefreshToken, AuthUser user);
    boolean isRefreshTokenValid(String rawRefreshToken, UUID userId);
    void deleteRefreshTokenByRawToken(AuthUser user, String rawToken);
    void deleteRefreshTokenByRawToken(UUID userId, String rawToken);
    long deleteAllRefreshTokensOfUser(AuthUser user);
    long deleteAllRefreshTokensOfUser(UUID userId);
    void deleteExpiredRefreshTokens();
}
