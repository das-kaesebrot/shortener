package eu.kaesebrot.dev.shortener.model;
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

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class ShortenerUser implements UserDetails, CredentialsContainer {
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

    @JsonProperty("is_enabled")
    private boolean enabled;

    @OneToMany(mappedBy="owner")
    @JsonManagedReference
    private Set<Link> links;

    @ElementCollection(targetClass = GrantedAuthority.class)
    @CollectionTable
    @JsonProperty("authorities")
    private Set<GrantedAuthority> authorities;

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
        this.authorities = Collections.synchronizedSet(Set.of());
        this.enabled = true;
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

        this.disableAccount();
        this.hashedConfirmationToken = hashedConfirmationToken;
    }

    public void setEmailVerified() {
        this.hashedConfirmationToken = null;
        this.enableAccount();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountExpiredAt == null || Timestamp.from(Instant.now()).before(accountExpiredAt);
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

    public void clearAccountExpired() {
        accountExpiredAt = null;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    public void lockAccount() {
        locked = true;
    }

    public void unlockAccount() {
        locked = false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsExpiredAt == null || Timestamp.from(Instant.now()).before(credentialsExpiredAt);
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

    public void clearCredentialsExpired() {
        credentialsExpiredAt = null;
    }

    public void enableAccount() {
        this.enabled = true;
    }

    public void disableAccount() {
        this.enabled = false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void eraseCredentials() {
        this.passwordHash = null;
    }
}
