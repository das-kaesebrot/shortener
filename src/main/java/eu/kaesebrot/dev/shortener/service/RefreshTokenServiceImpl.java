package eu.kaesebrot.dev.shortener.service;

import eu.kaesebrot.dev.shortener.model.AuthUser;
import eu.kaesebrot.dev.shortener.model.RefreshToken;
import eu.kaesebrot.dev.shortener.repository.AuthUserRepository;
import eu.kaesebrot.dev.shortener.repository.RefreshTokenRepository;
import eu.kaesebrot.dev.shortener.utils.RandomStringGenerator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RandomStringGenerator randomStringGenerator;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Optional<RefreshToken> getRefreshTokenByRawToken(AuthUser user, String rawRefreshToken, Instant expiresAt) {
        for (var token : refreshTokenRepository.findByUserAndExpiresAtAfter(user, expiresAt)) {
            if (passwordEncoder.matches(rawRefreshToken, token.getRefreshTokenHash())) {
                return Optional.of(token);
            }
        }

        return Optional.empty();
    }

    private Optional<RefreshToken> getRefreshTokenByRawToken(UUID userId, String rawRefreshToken, Instant expiresAt) {
        AuthUser user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return getRefreshTokenByRawToken(user, rawRefreshToken, expiresAt);
    }

    @Override
    @Transactional
    public String generateRefreshTokenForUser(AuthUser user, Instant expiresAt) {
        String rawRefreshToken = randomStringGenerator.generate(72);
        String encodedRefreshToken = passwordEncoder.encode(rawRefreshToken);

        RefreshToken refreshToken = new RefreshToken(user, encodedRefreshToken, expiresAt);
        refreshTokenRepository.save(refreshToken);

        return rawRefreshToken;
    }

    @Override
    public String generateRefreshTokenForUser(UUID userId, Instant expiresAt) {
        AuthUser user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The user could not be found"));
        return generateRefreshTokenForUser(user, expiresAt);
    }

    @Override
    public boolean isRefreshTokenValid(String rawRefreshToken, AuthUser user) {
        Instant now = Instant.now();

        for (var token : refreshTokenRepository.findByUserAndExpiresAtAfter(user, now)) {
            if (passwordEncoder.matches(rawRefreshToken, token.getRefreshTokenHash())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isRefreshTokenValid(String rawRefreshToken, UUID userId) {
        Instant now = Instant.now();

        for (var token : refreshTokenRepository.findByUserIdAndExpiresAtAfter(userId, now)) {
            if (passwordEncoder.matches(rawRefreshToken, token.getRefreshTokenHash())) {
                return true;
            }
        }

        return false;
    }

    @Override
    @Transactional
    public void deleteRefreshTokenByRawToken(AuthUser user, String rawToken) {
        RefreshToken token = getRefreshTokenByRawToken(user, rawToken, Instant.now()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The token could not be found"));
        refreshTokenRepository.delete(token);
    }

    @Override
    @Transactional
    public void deleteRefreshTokenByRawToken(UUID userId, String rawToken) {
        RefreshToken token = getRefreshTokenByRawToken(userId, rawToken, Instant.now()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The token could not be found"));
        refreshTokenRepository.delete(token);
    }

    @Override
    @Transactional
    public long deleteAllRefreshTokensOfUser(AuthUser user) {
        return refreshTokenRepository.deleteByUser(user);
    }

    @Override
    @Transactional
    public long deleteAllRefreshTokensOfUser(UUID userId) {
        return refreshTokenRepository.deleteByUserId(userId);
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
