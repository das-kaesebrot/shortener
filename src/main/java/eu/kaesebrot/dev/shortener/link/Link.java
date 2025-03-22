package eu.kaesebrot.dev.shortener.link;
import java.net.URI;
import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import eu.kaesebrot.dev.shortener.security.ShortenerUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Link {

    @Version
    private long Version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    private String shortUri;
    private URI fullUri;

    @NotNull
    private int hits;

    @ManyToOne
    @JoinColumn
    (name = "owner_id")
    private ShortenerUser owner;

    @CreationTimestamp
    @Column(nullable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Timestamp modifiedAt;

    public Link() {
        this.hits = 0;
    }

    public Link(String shortUri, URI fullUri, ShortenerUser owner) {
        this();
        this.shortUri = shortUri;
        this.fullUri = fullUri;
        this.owner = owner;
    }

    public Link(String shortUri, String fullUri, ShortenerUser owner) {
        this(shortUri, URI.create(fullUri), owner);
    }
    
    public long getVersion() {
        return Version;
    }

    public void setVersion(long version) {
        Version = version;
    }

    public Long getId() {
        return id;
    }

    public String getShortURI() {
        return shortUri;
    }

    public void setShortURI(String shortUri) {
        this.shortUri = shortUri;
    }

    public URI getFullURI() {
        return fullUri;
    }

    public void setFullURI(URI fullUri) {
        this.fullUri = fullUri;
    }

    public void setFullURI(String fullUri) {
        this.fullUri = URI.create(fullUri);
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
