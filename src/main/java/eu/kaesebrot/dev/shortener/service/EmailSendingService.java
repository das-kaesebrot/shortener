package eu.kaesebrot.dev.shortener.service;

public interface EmailSendingService {    
    void sendMessage(String to, String subject, String text);
}
