package eu.kaesebrot.dev.shortener.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class ShortenerUserDetailsService implements UserDetailsService {    
    @Autowired
    private ShortenerUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        ShortenerUser user;
        Optional<ShortenerUser> userOptional = userRepository.findById(username);

        if (!userOptional.isPresent()) {
            return null;
        }

        user = userOptional.get();

        return User.withUsername(username).password(user.getPassword()).roles(user.getRole()).build();
    }
}
