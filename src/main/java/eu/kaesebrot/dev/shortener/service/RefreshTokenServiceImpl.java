package eu.kaesebrot.dev.shortener.service;

import eu.kaesebrot.dev.shortener.model.AuthUser;
import eu.kaesebrot.dev.shortener.model.RefreshToken;
import eu.kaesebrot.dev.shortener.repository.AuthUserRepository;
import eu.kaesebrot.dev.shortener.repository.RefreshTokenRepository;
import eu.kaesebrot.dev.shortener.utils.RandomStringGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RandomStringGenerator randomStringGenerator;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public String generateRefreshTokenForUser(AuthUser user, Instant expiresAt) {
        String rawRefreshToken = randomStringGenerator.generateHexToken();
        String encodedRefreshToken = passwordEncoder.encode(rawRefreshToken);

        RefreshToken refreshToken = new RefreshToken(user, encodedRefreshToken, expiresAt);
        refreshTokenRepository.save(refreshToken);

        return rawRefreshToken;
    }

    @Override
    public String generateRefreshTokenForUser(String username, Instant expiresAt) {
        AuthUser user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The user could not be found"));
        return generateRefreshTokenForUser(user, expiresAt);
    }

    @Override
    public boolean validateRefreshToken(String rawRefreshToken, AuthUser user) {
        Instant now = Instant.now();

        for (var token : refreshTokenRepository.findByUserAndExpiresAtAfter(user, now)) {
            if (passwordEncoder.matches(rawRefreshToken, token.getRefreshTokenHash())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean validateRefreshToken(String rawRefreshToken, String username) {
        Instant now = Instant.now();

        for (var token : refreshTokenRepository.findByUserUsernameAndExpiresAtAfter(username, now)) {
            if (passwordEncoder.matches(rawRefreshToken, token.getRefreshTokenHash())) {
                return true;
            }
        }

        return false;
    }

    @Override
    @Transactional
    @Scheduled(cron = "${shortener.jwt.cleanup-interval:* * * * *}")
    public void deleteExpiredRefreshTokens() {
        logger.debug("Cleaning expired refresh tokens");
        long deletedTokens = refreshTokenRepository.deleteByExpiresAtBefore(Instant.now());
        logger.debug(String.format("Deleted %d tokens", deletedTokens));
    }
}
