package eu.kaesebrot.dev.shortener.service;

import java.net.URI;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import eu.kaesebrot.dev.shortener.model.ShortenerUser;
import eu.kaesebrot.dev.shortener.repository.ShortenerUserRepository;
import eu.kaesebrot.dev.shortener.utils.HexStringGenerator;

@Service
public class EmailConfirmationTokenServiceImpl implements EmailConfirmationTokenService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final ShortenerUserRepository shortenerUserRepository;
    private final HexStringGenerator hexStringGenerator;
    private final EmailSendingService emailSendingService;

    public EmailConfirmationTokenServiceImpl(ShortenerUserRepository shortenerUserRepository,
            HexStringGenerator hexStringGenerator, EmailSendingService emailSendingService) {
        this.shortenerUserRepository = shortenerUserRepository;
        this.hexStringGenerator = hexStringGenerator;
        this.emailSendingService = emailSendingService;
    }

    @Override
    public void generateAndSendConfirmationTokenToUser(ShortenerUser user, URI originalRequestUri,
            String tokenConfirmationPath) {
        String rawToken = hexStringGenerator.generateToken();
        user.updateHashedConfirmationToken(passwordEncoder.encode(rawToken));
        shortenerUserRepository.save(user);

        String portString = "";

        if (originalRequestUri.getPort() != -1) {
            portString = String.format(":%d", originalRequestUri.getPort());
        }

        String text = String.format("Please confirm your mail address by visiting the following page in your browser: %s://%s%s/%s/%s", originalRequestUri.getScheme(), originalRequestUri.getHost(), portString, tokenConfirmationPath, rawToken);
        String subject = "Please confirm your mail address";

        emailSendingService.sendMessage(user.getEmail(), subject, text);
    }

    @Override
    public void redeemToken(String rawToken) {
        String encodedToken = passwordEncoder.encode(rawToken);
        ShortenerUser user = shortenerUserRepository.findByHashedConfirmationToken(encodedToken).orElseThrow();
        user.setEmailVerified();
        shortenerUserRepository.save(user);
    }

}
