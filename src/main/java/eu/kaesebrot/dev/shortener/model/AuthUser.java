package eu.kaesebrot.dev.shortener.model;
import java.time.Instant;
import java.util.*;

import eu.kaesebrot.dev.shortener.enums.UserRole;
import eu.kaesebrot.dev.shortener.model.dto.response.AuthUserResponse;
import eu.kaesebrot.dev.shortener.utils.AuthUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = @Index(name = "un_index", columnList = "username"))
@Getter
public class AuthUser implements UserDetails, CredentialsContainer {
    @Version
    private long version;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(unique=true)
    @Setter
    private String username;

    private String passwordHash;

    @Email
    @Column(unique=true)
    @Setter
    private String email;

    @Column(nullable = true)
    private Instant accountExpiredAt;

    @Column(nullable = true)
    private Instant credentialsExpiredAt;

    private boolean locked;

    private boolean enabled;

    @OneToMany(mappedBy="owner")
    @Setter
    private Set<Link> links;

    @Setter
    private UserRole role;

    @Column(nullable = true)
    @Getter
    private String hashedConfirmationToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RefreshToken> refreshTokens = new HashSet<>();

    @CreatedDate
    @Column(nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant modifiedAt;

    public AuthUser(@NotBlank String username, @NotBlank String passwordHash, String email) {
        this();
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
    }

    public AuthUser() {
        this.links = new HashSet<>();
        this.enabled = true;
    }


    @Override
    public String getPassword() {
        return passwordHash;
    }

    public void setPassword(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void updateHashedConfirmationToken(String hashedConfirmationToken) {
        if (this.hashedConfirmationToken.equals(hashedConfirmationToken)) {
            return;
        }
        this.hashedConfirmationToken = hashedConfirmationToken;
    }

    public void setEmailVerified() {
        this.hashedConfirmationToken = null;
        this.enableAccount();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthUtils.convertScopesToAuthorities(AuthUtils.mapScopesFromUserRoleRecursively(role));
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

    public AuthUserResponse toDto() {
        return AuthUserResponse.fromAuthUser(this);
    }
}
