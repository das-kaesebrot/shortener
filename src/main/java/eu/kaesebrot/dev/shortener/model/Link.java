package eu.kaesebrot.dev.shortener.model;
import java.io.Serializable;
import java.net.URI;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import eu.kaesebrot.dev.shortener.utils.StringUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Link implements Serializable {
    @Version
    private long version;

    @Id
    @Column(updatable = false, nullable = false)
    private String id;

    @JsonProperty("redirect_uri")
    private URI redirectUri;

    @NotNull
    private int hits;

    @ManyToOne
    @JoinColumn
    (name = "owner_id")
    @JsonBackReference
    @Setter
    private AuthUser owner;

    @CreationTimestamp
    @Column(nullable = false)
    @JsonProperty("created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    @JsonProperty("modified_at")
    private Timestamp modifiedAt;

    public Link() {
        this.hits = 0;
    }

    public Link(String shortUri, URI redirectUri, AuthUser owner) {
        this();
        this.id = shortUri;
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
