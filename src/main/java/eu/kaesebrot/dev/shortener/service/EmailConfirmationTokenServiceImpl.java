package eu.kaesebrot.dev.shortener.service;

import java.net.URI;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import eu.kaesebrot.dev.shortener.model.AuthUser;
import eu.kaesebrot.dev.shortener.repository.ShortenerUserRepository;
import eu.kaesebrot.dev.shortener.utils.RandomStringGenerator;

@Service
@RequiredArgsConstructor
public class EmailConfirmationTokenServiceImpl implements EmailConfirmationTokenService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final ShortenerUserRepository shortenerUserRepository;
    private final RandomStringGenerator randomStringGenerator;
    private final EmailSendingService emailSendingService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void generateAndSendConfirmationTokenToUser(AuthUser user, URI originalRequestUri,
                                                       String tokenConfirmationPath) {
        String rawToken = randomStringGenerator.generateHexToken();
        user.updateHashedConfirmationToken(passwordEncoder.encode(rawToken));
        shortenerUserRepository.save(user);

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
    public void redeemToken(AuthUser user, String rawToken) {
        if (!passwordEncoder.matches(rawToken, user.getHashedConfirmationToken())) {
            throw new IllegalArgumentException("Token doesn't match!");
        }
        user.setEmailVerified();
        shortenerUserRepository.save(user);
    }

}
