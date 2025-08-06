package eu.kaesebrot.dev.shortener.service;

import java.net.URI;

import eu.kaesebrot.dev.shortener.config.ShortenerConfig;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import eu.kaesebrot.dev.shortener.model.AuthUser;
import eu.kaesebrot.dev.shortener.repository.AuthUserRepository;
import eu.kaesebrot.dev.shortener.utils.RandomStringGenerator;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailConfirmationTokenServiceImpl implements EmailConfirmationTokenService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AuthUserRepository authUserRepository;
    private final RandomStringGenerator randomStringGenerator;
    private final EmailSendingService emailSendingService;
    private final ShortenerConfig shortenerConfig;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    @Async
    public void generateAndSendConfirmationTokenToUser(AuthUser user, URI originalRequestUri,
                                                       String tokenConfirmationPath) {
        if (!shortenerConfig.isEmailVerificationRequired()) {
            logger.warn("Mail verification is disabled, skipping sending mail to user");
            return;
        }

        String rawToken = randomStringGenerator.generateHexToken();
        user.updateHashedConfirmationToken(passwordEncoder.encode(rawToken));
        user.disableAccount();
        authUserRepository.save(user);

        String portString = "";

        if (originalRequestUri.getPort() != -1) {
            portString = String.format(":%d", originalRequestUri.getPort());
        }

        String text = String.format(
                "Please confirm your mail address by visiting the following page in your browser: %s://%s%s/%s/%s",
                originalRequestUri.getScheme(), originalRequestUri.getHost(), portString, tokenConfirmationPath,
                rawToken);
        String subject = "Please confirm your mail address";

        emailSendingService.sendMessage(user.getEmail(), subject, text);
    }

    @Override
    @Transactional
    public void redeemToken(AuthUser user, String rawToken) {
        if (!shortenerConfig.isEmailVerificationRequired()) return;

        if (!passwordEncoder.matches(rawToken, user.getHashedConfirmationToken())) {
            throw new IllegalArgumentException("Token doesn't match!");
        }
        user.setEmailVerified();
        authUserRepository.save(user);
    }
}
