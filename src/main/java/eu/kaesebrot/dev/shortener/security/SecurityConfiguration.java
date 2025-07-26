package eu.kaesebrot.dev.shortener.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.
                authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/ui", "/api/v1/shortener/links/redirect/**", "/s/*", "/api/swagger-ui/**", "/api/docs/**", "/error", "/ui/assets/**").permitAll()
                        .requestMatchers("/ui/dashboard/admin/**").hasRole("ADMIN")
                        .requestMatchers("/ui/dashboard/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/ui/login")
                        .failureUrl("/ui/login?error=true")
                        .loginProcessingUrl("/login")
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/ui/logout")
                        .logoutSuccessUrl("/ui")
                        .deleteCookies("JSESSIONID")
                        .permitAll())
        ;

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
