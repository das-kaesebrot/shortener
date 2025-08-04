package eu.kaesebrot.dev.shortener.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.kaesebrot.dev.shortener.model.Link;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.net.URI;
import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
public class LinkResponse implements Serializable {
    @JsonProperty("id")
    private UUID Id;

    @JsonProperty("short_uri")
    private String shortUri;

    @JsonProperty("redirect_uri")
    private URI redirectUri;

    @NotNull
    private int hits;

    @JsonProperty("owner")
    private String ownerUsername;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("modified_at")
    private Instant modifiedAt;

    public static LinkResponse fromLink(Link link) {
        return new LinkResponse(link.getId(), link.getShortUri(), link.getRedirectUri(), link.getHits(), link.getOwner().getUsername(), link.getCreatedAt(), link.getModifiedAt());
    }
}
