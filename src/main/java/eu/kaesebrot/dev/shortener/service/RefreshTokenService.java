package eu.kaesebrot.dev.shortener.service;

import eu.kaesebrot.dev.shortener.model.AuthUser;

import java.time.Instant;
import java.util.UUID;

public interface RefreshTokenService {
    String generateRefreshTokenForUser(AuthUser user, Instant expiresAt);
    String generateRefreshTokenForUser(UUID userId, Instant expiresAt);
    String generateRefreshTokenForUser(String username, Instant expiresAt);
    boolean validateRefreshToken(String rawRefreshToken, AuthUser user);
    boolean validateRefreshToken(String rawRefreshToken, String username);
    long deleteExpiredRefreshTokens();
}
