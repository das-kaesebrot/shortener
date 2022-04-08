package de.ksbrt.shortener.link;
import de.ksbrt.shortener.security.ShortenerUser;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Link {

    @Version
    private long Version;
    
    @Id
	@GenericGenerator(
		name = "UUID",
		strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID Id;

    @NotBlank(message = "{notEmpty}")
    private String ShortUri;

    @NotBlank(message = "{notEmpty}")
    private String FullUri;

    @NotNull
    private int Hits;

    @ManyToOne
    @JoinColumn
    (name = "OWNER_ID")
    private ShortenerUser Owner;

    public Link(String shortUri, String fullUri, ShortenerUser owner) {
        ShortUri = shortUri;
        FullUri = fullUri;
        Owner = owner;
        Hits = 0;
    }
    
    public long getVersion() {
        return Version;
    }

    public void setVersion(long version) {
        Version = version;
    }

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    public String getShortURI() {
        return ShortUri;
    }

    public void setShortURI(String shortUri) {
        ShortUri = shortUri;
    }

    public String getFullURI() {
        return FullUri;
    }

    public void setFullURI(String fullUri) {
        FullUri = fullUri;
    }

    public int getHits() {
        return Hits;
    }

    public void setHits(int hits) {
        Hits = hits;
    }
    
    public ShortenerUser getOwner() {
        return Owner;
    }

    public void setOwner(ShortenerUser owner) {
        Owner = owner;
    }

    public void incrementHits() {
        Hits++;
    }
}
