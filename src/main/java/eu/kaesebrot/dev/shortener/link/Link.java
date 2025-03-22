package eu.kaesebrot.dev.shortener.link;
import eu.kaesebrot.dev.shortener.security.ShortenerUser;
import java.util.UUID;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Link {

    @Version
    private long Version;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid4")
	private UUID Id;

    @NotBlank(message = "{notEmpty}")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String ShortUri;

    @NotBlank(message = "{notEmpty}")
    private String FullUri;

    @NotNull
    private int Hits;

    @ManyToOne
    @JoinColumn
    (name = "OWNER_ID")
    private ShortenerUser Owner;

    public Link() {}

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
