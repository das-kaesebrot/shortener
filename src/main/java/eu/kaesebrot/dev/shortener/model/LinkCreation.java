package eu.kaesebrot.dev.shortener.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class LinkCreation implements Serializable {
    @NotNull
    @JsonProperty("redirect_uri")
    private String redirectUri;

    @JsonProperty("id")
    private String id;

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
