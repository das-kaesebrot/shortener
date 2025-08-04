package eu.kaesebrot.dev.shortener.model;
import java.io.Serializable;
import java.net.URI;
import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import eu.kaesebrot.dev.shortener.utils.StringUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Link implements Serializable {
    @Version
    private long version;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID Id;

    @Column(unique = true, nullable = false)
    private String shortUri;

    private URI redirectUri;

    @NotNull
    private int hits;

    @ManyToOne
    @JoinColumn
    (name = "owner_id")
    @JsonBackReference
    @Setter
    private AuthUser owner;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JsonProperty("created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    @JsonProperty("modified_at")
    private Instant modifiedAt;

    public Link() {
        this.hits = 0;
    }

    public Link(String shortUri, URI redirectUri, AuthUser owner) {
        this();
        this.shortUri = shortUri;
        setRedirectUri(redirectUri);
        this.owner = owner;
    }

    public Link(String shortUri, String redirectUri, AuthUser owner) {
        this(shortUri, URI.create(redirectUri), owner);
    }

    public void setRedirectUri(String redirectUri) {
        setRedirectUri(URI.create(redirectUri));
    }

    public void setRedirectUri(URI redirectUri) {
        setRedirectUri(redirectUri, false);
    }

    public void setRedirectUri(URI redirectUri, boolean checkScheme) {
        if (StringUtils.isNullOrEmpty(redirectUri.getScheme()) && checkScheme) {
            String redirectUriStr = redirectUri.toString();
            redirectUriStr = String.format("%s://%s", "https", redirectUriStr);
            redirectUri = URI.create(redirectUriStr);
        }
        this.redirectUri = redirectUri;
    }

    public void incrementHits() {
        hits++;
    }
}
