package eu.kaesebrot.dev.shortener.service;

import eu.kaesebrot.dev.shortener.repository.ShortenerUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {
    private final ShortenerUserRepository _userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var matchedUser = _userRepository.findByUsername(username);

        if (matchedUser.isEmpty()) {
            matchedUser = _userRepository.findByEmail(username);
        }

        return matchedUser.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
