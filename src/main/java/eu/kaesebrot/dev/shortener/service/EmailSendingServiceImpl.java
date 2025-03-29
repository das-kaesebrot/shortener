package eu.kaesebrot.dev.shortener.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import eu.kaesebrot.dev.shortener.properties.MailProperties;

@Service
public class EmailSendingServiceImpl implements EmailSendingService {

    private final JavaMailSender javaMailSender;
    private final String from;

    public EmailSendingServiceImpl(JavaMailSender javaMailSender, MailProperties mailProperties) {
        this.javaMailSender = javaMailSender;
        this.from = mailProperties.getFrom();
    }

    @Override
    public void sendMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }
}
