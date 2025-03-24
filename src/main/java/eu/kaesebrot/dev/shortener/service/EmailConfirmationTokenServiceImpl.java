package eu.kaesebrot.dev.shortener.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import eu.kaesebrot.dev.shortener.model.EmailConfirmationToken;
import eu.kaesebrot.dev.shortener.model.ShortenerUser;
import eu.kaesebrot.dev.shortener.repository.EmailConfirmationTokenRepository;
import eu.kaesebrot.dev.shortener.utils.HexStringGenerator;

@Service
public class EmailConfirmationTokenServiceImpl implements EmailConfirmationTokenService {

    private final BCryptPasswordEncoder _passwordEncoder = new BCryptPasswordEncoder();
    private final EmailConfirmationTokenRepository _confirmationTokenRepository;
    private final HexStringGenerator _hexStringGenerator;

    public EmailConfirmationTokenServiceImpl(EmailConfirmationTokenRepository confirmationTokenRepository, HexStringGenerator hexStringGenerator) {
        _confirmationTokenRepository = confirmationTokenRepository;
        _hexStringGenerator = hexStringGenerator;
    }

    @Override
    public String generateConfirmationTokenForUser(ShortenerUser user) {
        String rawToken = _hexStringGenerator.generateToken();

        EmailConfirmationToken token = new EmailConfirmationToken(_passwordEncoder.encode(rawToken), user);
        _confirmationTokenRepository.save(token);

        return rawToken;
    }

    @Override
    public ShortenerUser redeemToken(String rawToken) {
        String encodedToken = _passwordEncoder.encode(rawToken);

        EmailConfirmationToken token = _confirmationTokenRepository.findById(encodedToken).orElseThrow();
        ShortenerUser associatedUser = token.getAssociatedUser();
        _confirmationTokenRepository.delete(token);

        return associatedUser;
    }
    
}
