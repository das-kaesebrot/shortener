package eu.kaesebrot.dev.shortener.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private AuthUser user;

    private String refreshTokenHash;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant expiresAt;

    public RefreshToken(AuthUser user, String refreshTokenHash, Instant expiresAt) {
        this.user = user;
        this.refreshTokenHash = refreshTokenHash;
        this.expiresAt = expiresAt;
    }
}
