package eu.kaesebrot.dev.shortener.security;

import eu.kaesebrot.dev.shortener.service.AuthUserDetailsService;
import lombok.RequiredArgsConstructor;
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
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.oauth2.core.authorization.OAuth2AuthorizationManagers.hasScope;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final AuthUserDetailsService _authUserDetailsService;
    private final BCryptPasswordEncoder _passwordEncoder = new BCryptPasswordEncoder();

    // TODO
    @Value("${shortener.hosting.subdirectory:}")
    private final String subdirectory = "";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        // API docs, error paths
                        .requestMatchers(HttpMethod.GET, "/api/swagger-ui/**", "/api/docs/**", "/error").permitAll()
                        //
                        // --- AUTH controller ---
                        //
                        // user account confirmation via secret token
                        .requestMatchers(HttpMethod.GET, "/api/v1/auth/users/*/confirm/*").permitAll()
                        // user account creation
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/users").permitAll()
                        // jwt retrieval and refresh
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login", "/api/v1/auth/refresh").permitAll()
                        // self management
                        .requestMatchers("/api/v1/auth/me").access(hasScope("self"))
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/logout", "/api/v1/auth/revoke").access(hasScope("self.logout"))
                        // admin management
                        .requestMatchers(HttpMethod.GET, "/api/api/v1/auth/users/*").access(hasScope("users.read"))
                        .requestMatchers(HttpMethod.DELETE, "/api/api/v1/auth/users/*").access(hasScope("users.write"))
                        //
                        // --- LINKS controller ---
                        //
                        // shortlink resolution
                        .requestMatchers(HttpMethod.GET, "/api/v1/links/*/redirect", "/s/*").permitAll()
                        // link management
                        .requestMatchers("/api/v1/links/*").access(hasScope("links"))
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer((oauth2) -> oauth2
                        .jwt(withDefaults())
                        .authenticationEntryPoint(
                                new BearerTokenAuthenticationEntryPoint())
                        .accessDeniedHandler(
                                new BearerTokenAccessDeniedHandler())
                )
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

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("scope");
        grantedAuthoritiesConverter.setAuthorityPrefix("SCOPE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
