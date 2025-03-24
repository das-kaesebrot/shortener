package eu.kaesebrot.dev.shortener.model;

import java.io.Serializable;
import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class EmailConfirmationToken implements Serializable {
    @Id
    private String hashedToken;

    @OneToOne(mappedBy = "emailConfirmationToken")
    private ShortenerUser associatedUser;

    @CreationTimestamp
    @Column(nullable = false)
    private Timestamp createdAt;

    public EmailConfirmationToken() {

    }

    public EmailConfirmationToken(String hashedToken, ShortenerUser associatedUser) {
        this();
        this.hashedToken = hashedToken;
        this.associatedUser = associatedUser;
    }

    public String getHashedToken() {
        return hashedToken;
    }

    public ShortenerUser getAssociatedUser() {
        return associatedUser;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }    
}
