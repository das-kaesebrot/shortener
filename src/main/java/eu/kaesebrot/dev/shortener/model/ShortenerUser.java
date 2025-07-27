package eu.kaesebrot.dev.shortener.model;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import eu.kaesebrot.dev.shortener.enums.UserState;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class ShortenerUser implements Serializable, UserDetails {
    @Version
    @JsonIgnore
    private long version;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @NotBlank
    @Column(unique=true)
    private String username;

    @NotBlank
    @JsonIgnore
    private String passwordHash;

    @NotBlank
    @Email
    @Column(unique=true)
    private String email;

    @Column(nullable = true)
    @JsonProperty("account_expired_at")
    private Timestamp accountExpiredAt;

    @Column(nullable = true)
    @JsonProperty("credentials_expired_at")
    private Timestamp credentialsExpiredAt;

    @JsonProperty("is_locked")
    private boolean locked;

    @OneToMany(mappedBy="owner")
    @JsonManagedReference
    private Set<Link> links;
    
    @ElementCollection(targetClass = UserState.class)
    @CollectionTable
    @Enumerated(EnumType.STRING)
    @JsonProperty("user_state")
    private Set<UserState> userState;

    @Column(nullable = true)
    @JsonIgnore
    private String hashedConfirmationToken;

    @CreationTimestamp
    @Column(nullable = false)
    @JsonProperty("created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    @JsonProperty("modified_at")
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

    @Override
    public String getPassword() {
        return passwordHash;
    }

    public void setPassword(String passwordHash) {
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

    public long getVersion() {
        return version;
    }

    public UUID getId() {
        return id;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getModifiedAt() {
        return modifiedAt;
    }

    public String getHashedConfirmationToken() {
        return hashedConfirmationToken;
    }

    public void updateHashedConfirmationToken(String hashedConfirmationToken) {
        if (this.hashedConfirmationToken == hashedConfirmationToken) {
            return;
        }

        addState(UserState.CONFIRMING_EMAIL);
        this.hashedConfirmationToken = hashedConfirmationToken;
    }

    public void setEmailVerified() {
        this.hashedConfirmationToken = null;
        removeState(UserState.CONFIRMING_EMAIL);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountExpiredAt.before(Timestamp.from(Instant.now()));
    }

    public void setAccountExpired() {
        if (accountExpiredAt != null) {
            return;
        }

        accountExpiredAt = Timestamp.from(Instant.now());
    }

    public void setAccountExpired(Instant instant) {
        accountExpiredAt = Timestamp.from(instant);
    }

    @Override
    public boolean isAccountNonLocked() {
        return locked;
    }

    public void lockAccount() {
        locked = true;
    }

    public void unlockAccount() {
        locked = false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsExpiredAt.before(Timestamp.from(Instant.now()));
    }

    public void setCredentialsExpired() {
        if (credentialsExpiredAt != null) {
            return;
        }

        credentialsExpiredAt = Timestamp.from(Instant.now());
    }

    public void setCredentialsExpired(Instant instant) {
        credentialsExpiredAt = Timestamp.from(instant);
    }

    @Override
    public boolean isEnabled() {
        // TODO
        return true;
    }
}
