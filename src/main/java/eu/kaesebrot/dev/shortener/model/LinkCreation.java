package eu.kaesebrot.dev.shortener.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class LinkCreation implements Serializable {
    @NotNull
    @JsonProperty("redirect_uri")
    private String redirectUri;

    @JsonProperty("id")
    @Size(min = 5, max = 32)
    @Pattern(regexp = "^[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]$", message = "Id is in wrong format! Has to be 5-32 chars long and only containing lowercase chars, uppercase chars, digits or dashes. Id cannot start or end with a dash.")
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
