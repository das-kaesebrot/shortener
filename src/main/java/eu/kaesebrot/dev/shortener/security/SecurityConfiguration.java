package eu.kaesebrot.dev.shortener.security;

import eu.kaesebrot.dev.shortener.service.AuthUserDetailsService;
 import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final AuthUserDetailsService _authUserDetailsService;
    private final BCryptPasswordEncoder _passwordEncoder;

    // TODO
    @Value("${shortener.hosting.subdirectory:}")
    private final String subdirectory = "";

    public SecurityConfiguration(AuthUserDetailsService authUserDetailsService) {
        _authUserDetailsService = authUserDetailsService;
        _passwordEncoder = new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        // API docs, error paths
                        .requestMatchers(HttpMethod.GET, "/api/swagger-ui/**", "/api/docs/**", "/error").permitAll()
                        // shortlink resolution
                        .requestMatchers(HttpMethod.GET, "/api/v1/links/*/redirect", "/s/*").permitAll()
                        // user account confirmation via secret token
                        .requestMatchers(HttpMethod.GET, "/api/v1/auth/users/*/confirm/*").permitAll()
                        // user account creation
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/users").permitAll()
                        // jwt retrieval and refresh
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login", "/api/v1/auth/refresh").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(withDefaults()))
        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return _passwordEncoder;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return _authUserDetailsService;
    }

    @Bean
    public AuthenticationManager authManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        var authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authProvider);
    }
}
