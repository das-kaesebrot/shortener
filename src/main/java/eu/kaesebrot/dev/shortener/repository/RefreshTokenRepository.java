package eu.kaesebrot.dev.shortener.repository;

import eu.kaesebrot.dev.shortener.model.AuthUser;
import eu.kaesebrot.dev.shortener.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    List<RefreshToken> findByUserAndExpiresAtAfter(AuthUser user, Instant expiresAtAfter);
    List<RefreshToken> findByUserIdAndExpiresAtAfter(UUID userId, Instant expiresAtAfter);
    long deleteByExpiresAtBefore(Instant expiresAt);
    long deleteByUserId(UUID userId);
    long deleteByUser(AuthUser user);
}
