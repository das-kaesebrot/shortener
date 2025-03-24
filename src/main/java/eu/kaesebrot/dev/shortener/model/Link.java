package eu.kaesebrot.dev.shortener.model;
import java.io.Serializable;
import java.net.URI;
import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import eu.kaesebrot.dev.shortener.util.StringUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
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
    private ShortenerUser owner;

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

    public Link(String shortUri, URI redirectUri, ShortenerUser owner) {
        this();
        this.id = shortUri;
        if (StringUtils.isNullOrEmpty(redirectUri.getScheme())) {
            String redirectUriStr = redirectUri.toString();
            redirectUriStr = String.format("%s://%s", "https", redirectUriStr);
            redirectUri = URI.create(redirectUriStr);
        }
        this.redirectUri = redirectUri;
        this.owner = owner;
    }

    public Link(String shortUri, String redirectUri, ShortenerUser owner) {
        this(shortUri, URI.create(redirectUri), owner);
    }
    
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public URI getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(URI redirectUri) {
        this.redirectUri = redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.setRedirectUri(URI.create(redirectUri));
    }

    public int getHits() {
        return hits;
    }

    public ShortenerUser getOwner() {
        return owner;
    }

    public void setOwner(ShortenerUser owner) {
        this.owner = owner;
    }

    public void incrementHits() {
        hits++;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getModifiedAt() {
        return modifiedAt;
    }
}
