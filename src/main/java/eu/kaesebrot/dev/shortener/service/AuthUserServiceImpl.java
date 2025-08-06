package eu.kaesebrot.dev.shortener.service;

import eu.kaesebrot.dev.shortener.model.AuthUser;
import eu.kaesebrot.dev.shortener.repository.AuthUserRepository;
import eu.kaesebrot.dev.shortener.utils.RandomStringGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {
    private final AuthUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RandomStringGenerator randomStringGenerator;

    @Override
    public String createNewUserAndReturnGeneratedPassword(String username, String email) {
        final String rawPassword = randomStringGenerator.generate(72);
        createNewUser(username, email, rawPassword);
        return rawPassword;
    }

    @Override
    @Transactional
    public AuthUser createNewUser(String username, String email, String rawPassword) {
        AuthUser user = new AuthUser(username, passwordEncoder.encode(rawPassword), email);
        return userRepository.save(user);
    }
}
