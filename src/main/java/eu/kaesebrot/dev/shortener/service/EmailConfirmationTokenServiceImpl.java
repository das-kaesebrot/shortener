package eu.kaesebrot.dev.shortener.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import eu.kaesebrot.dev.shortener.model.ShortenerUser;
import eu.kaesebrot.dev.shortener.repository.ShortenerUserRepository;
import eu.kaesebrot.dev.shortener.utils.HexStringGenerator;

@Service
public class EmailConfirmationTokenServiceImpl implements EmailConfirmationTokenService {

    private final BCryptPasswordEncoder _passwordEncoder = new BCryptPasswordEncoder();
    private final ShortenerUserRepository _shortenerUserRepository;
    private final HexStringGenerator _hexStringGenerator;

    public EmailConfirmationTokenServiceImpl(ShortenerUserRepository shortenerUserRepository, HexStringGenerator hexStringGenerator) {
        _shortenerUserRepository = shortenerUserRepository;
        _hexStringGenerator = hexStringGenerator;
    }

    @Override
    public String generateConfirmationTokenForUser(ShortenerUser user) {
        String rawToken = _hexStringGenerator.generateToken();
        user.updateHashedConfirmationToken(_passwordEncoder.encode(rawToken));
        _shortenerUserRepository.save(user);
        return rawToken;
    }

    @Override
    public void redeemToken(String rawToken) {
        String encodedToken = _passwordEncoder.encode(rawToken);
        ShortenerUser user = _shortenerUserRepository.findByHashedConfirmationToken(encodedToken).orElseThrow();
        user.setEmailVerified();
        _shortenerUserRepository.save(user);
    }
    
}
