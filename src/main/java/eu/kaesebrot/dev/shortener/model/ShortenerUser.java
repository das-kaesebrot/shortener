package eu.kaesebrot.dev.shortener.model;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import eu.kaesebrot.dev.shortener.enums.UserState;

@Entity
public class ShortenerUser implements Serializable {
    @Version
    private long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @NotBlank
    @Column(unique=true)
    private String username;

    @NotBlank
    private String passwordHash;

    @NotBlank
    @Email
    @Column(unique=true)
    private String email;

    @OneToMany(mappedBy="owner")
    private Set<Link> links;
    
    @ElementCollection(targetClass = UserState.class)
    @CollectionTable
    @Enumerated(EnumType.STRING)
    private Set<UserState> userState;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "confirmationtoken_id")
    private EmailConfirmationToken emailConfirmationToken;

    @CreationTimestamp
    @Column(nullable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Timestamp modifiedAt;

    public ShortenerUser(@NotBlank String username, @NotBlank String passwordHash, String email) {
        this();
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
    }

    public ShortenerUser() {
        this.links = new HashSet<>();
        this.userState = Collections.synchronizedSet(EnumSet.noneOf(UserState.class));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public Set<Link> getLinks() {
        return links;
    }

    public void setLinks(Set<Link> links) {
        this.links = links;
    }

    public void setState(EnumSet<UserState> userStateSet) {
        this.userState = userStateSet;
    }
    
    public void addState(UserState userState) {
        this.userState.add(userState);
    }

    public void removeState(UserState userState) {
        this.userState.remove(userState);
    }

    public void clearState() {
        this.setState(EnumSet.noneOf(UserState.class));
    }

    public Set<UserState> getState() {
        return userState;
    }
    
    public boolean hasState(UserState state) {
        return userState.contains(state);
    }
}
