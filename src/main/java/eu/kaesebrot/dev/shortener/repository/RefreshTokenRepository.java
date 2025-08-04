package eu.kaesebrot.dev.shortener.repository;

import eu.kaesebrot.dev.shortener.model.AuthUser;
import eu.kaesebrot.dev.shortener.model.RefreshToken;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    List<RefreshToken> findByUserAndExpiresAtAfter(AuthUser user, Instant expiresAtAfter);
    List<RefreshToken> findByUserUsernameAndExpiresAtAfter(@NotBlank String username, Instant expiresAt);
    long deleteByExpiresAtBefore(Instant expiresAt);
    long deleteByUserUsername(String username);
    long deleteByUser(AuthUser user);
}
