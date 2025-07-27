package eu.kaesebrot.dev.shortener.service;

import eu.kaesebrot.dev.shortener.repository.ShortenerUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthUserDetailsService implements UserDetailsService {
    private final ShortenerUserRepository _userRepository;

    public AuthUserDetailsService(ShortenerUserRepository userRepository) {
        _userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        return _userRepository
                .findByUsername(usernameOrEmail)
                .orElse(_userRepository
                        .findByEmail(usernameOrEmail)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + usernameOrEmail))
                );
    }
}
