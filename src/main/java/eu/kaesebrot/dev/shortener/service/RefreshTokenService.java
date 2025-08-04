package eu.kaesebrot.dev.shortener.service;

import eu.kaesebrot.dev.shortener.model.AuthUser;

import java.time.Instant;

public interface RefreshTokenService {
    String generateRefreshTokenForUser(AuthUser user, Instant expiresAt);
    String generateRefreshTokenForUser(String username, Instant expiresAt);
    boolean isRefreshTokenValid(String rawRefreshToken, AuthUser user);
    boolean isRefreshTokenValid(String rawRefreshToken, String username);
    void deleteRefreshTokenByRawToken(AuthUser user, String rawToken);
    void deleteRefreshTokenByRawToken(String username, String rawToken);
    long deleteAllRefreshTokensOfUser(AuthUser user);
    long deleteAllRefreshTokensOfUser(String username);
    void deleteExpiredRefreshTokens();
}
