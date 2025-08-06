package eu.kaesebrot.dev.shortener.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.kaesebrot.dev.shortener.config.ShortenerConfig;
import eu.kaesebrot.dev.shortener.enums.UserRole;
import eu.kaesebrot.dev.shortener.model.AuthUser;
import eu.kaesebrot.dev.shortener.repository.AuthUserRepository;
import eu.kaesebrot.dev.shortener.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AdminAccountCreator {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AuthUserRepository userRepository;
    private final AuthUserService userService;
    private final ShortenerConfig shortenerConfig;
    private final ObjectMapper objectMapper;

    @EventListener
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) throws JsonProcessingException {
        if (userRepository.count() > 0) {
            return;
        }

        logger.info("User repository is empty, creating admin account");

        String rawPassword = shortenerConfig.getAdminPassword();
        AuthUser user;

        if (StringUtils.isNullOrEmpty(rawPassword)) {
            rawPassword = userService.createNewUserAndReturnGeneratedPassword(shortenerConfig.getAdminUsername(), shortenerConfig.getAdminEmail());
            user = userRepository.findByUsername(shortenerConfig.getAdminUsername()).get();
        } else {
            user = userService.createNewUser(shortenerConfig.getAdminUsername(), shortenerConfig.getAdminEmail(), rawPassword);
            rawPassword = "xxx";
        }
        user.setEmailVerified();
        user.setRole(UserRole.SUPERADMIN);

        userRepository.save(user);

        var dtoString = objectMapper.writeValueAsString(user.toDto());

        logger.warn(String.format("Created admin account!\n\n\t\tUser DTO: %s\n\n\t\tUser ID: %s\n\t\tUsername: %s\n\t\tMail: %s\n\t\tPassword: %s\n\n", dtoString, user.getId(), user.getUsername(), user.getEmail(), rawPassword));
    }
}
