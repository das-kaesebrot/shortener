package eu.kaesebrot.dev.shortener.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import eu.kaesebrot.dev.shortener.properties.MailProperties;
import eu.kaesebrot.dev.shortener.utils.StringUtils;

@Configuration
public class MailSenderConfig {
    private final MailProperties mailProperties;

    public MailSenderConfig(MailProperties mailProperties) {
        this.mailProperties = mailProperties;
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailProperties.getHost());
        mailSender.setPort(mailProperties.getPort());
        
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        
        if (StringUtils.isNullOrEmpty(mailProperties.getUsername()) && StringUtils.isNullOrEmpty(mailProperties.getPassword())) {
            mailSender.setUsername(mailProperties.getUsername());
            mailSender.setPassword(mailProperties.getPassword());
            props.put("mail.smtp.auth", "true");
        }

        props.put("mail.smtp.starttls.enable", mailProperties.getStarttls() ? "true" : "false");
        props.put("mail.smtp.starttls.required", mailProperties.getStarttls() ? "true" : "false");
        props.put("mail.smtp.ssl.enable", mailProperties.getSsl() ? "true" : "false");
        
        return mailSender;
    }
}
