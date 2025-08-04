package eu.kaesebrot.dev.shortener.model;
import java.time.Instant;
import java.util.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class AuthUser implements UserDetails, CredentialsContainer {
    @Version
    @JsonIgnore
    @Getter
    private long version;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    @Getter
    private UUID id;

    @NotBlank
    @Column(unique=true)
    @Getter
    @Setter
    private String username;

    @NotBlank
    @JsonIgnore
    private String passwordHash;

    @NotBlank
    @Email
    @Column(unique=true)
    @Getter
    private String email;

    @Column(nullable = true)
    @JsonProperty("account_expired_at")
    private Instant accountExpiredAt;

    @Column(nullable = true)
    @JsonProperty("credentials_expired_at")
    private Instant credentialsExpiredAt;

    @JsonProperty("is_locked")
    private boolean locked;

    @JsonProperty("is_enabled")
    private boolean enabled;

    @OneToMany(mappedBy="owner")
    @JsonManagedReference
    @Getter
    @Setter
    private Set<Link> links;

    @ElementCollection(targetClass = GrantedAuthority.class)
    @CollectionTable
    @JsonProperty("authorities")
    @Setter
    private Set<GrantedAuthority> authorities;

    @Column(nullable = true)
    @JsonIgnore
    @Getter
    private String hashedConfirmationToken;

    @CreatedDate
    @Column(nullable = false)
    @JsonProperty("created_at")
    @Getter
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    @JsonProperty("modified_at")
    @Getter
    private Instant modifiedAt;

    public AuthUser(@NotBlank String username, @NotBlank String passwordHash, String email) {
        this();
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
    }

    public AuthUser() {
        this.links = new HashSet<>();
        this.authorities = Collections.synchronizedSet(Set.of());
        this.enabled = true;
    }


    @Override
    public String getPassword() {
        return passwordHash;
    }

    public void setPassword(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void updateEmail(String email) {
        this.email = email;
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

    @Override
    public boolean isAccountNonExpired() {
        return accountExpiredAt == null || Instant.now().isBefore(accountExpiredAt);
    }

    public void setAccountExpired() {
        if (accountExpiredAt != null) {
            return;
        }

        accountExpiredAt = Instant.now();
    }

    public void setAccountExpired(Instant instant) {
        accountExpiredAt = instant;
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
        return credentialsExpiredAt == null || Instant.now().isBefore(credentialsExpiredAt);
    }

    public void setCredentialsExpired() {
        if (credentialsExpiredAt != null) {
            return;
        }

        credentialsExpiredAt = Instant.now();
    }

    public void setCredentialsExpired(Instant instant) {
        credentialsExpiredAt = instant;
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
