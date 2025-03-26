package eu.kaesebrot.dev.shortener.service;

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

    public EmailConfirmationTokenServiceImpl(ShortenerUserRepository shortenerUserRepository, HexStringGenerator hexStringGenerator) {
        this.shortenerUserRepository = shortenerUserRepository;
        this.hexStringGenerator = hexStringGenerator;
    }

    @Override
    public String generateConfirmationTokenForUser(ShortenerUser user) {
        String rawToken = hexStringGenerator.generateToken();
        user.updateHashedConfirmationToken(passwordEncoder.encode(rawToken));
        shortenerUserRepository.save(user);
        return rawToken;
    }

    @Override
    public void redeemToken(String rawToken) {
        String encodedToken = passwordEncoder.encode(rawToken);
        ShortenerUser user = shortenerUserRepository.findByHashedConfirmationToken(encodedToken).orElseThrow();
        user.setEmailVerified();
        shortenerUserRepository.save(user);
    }
    
}
