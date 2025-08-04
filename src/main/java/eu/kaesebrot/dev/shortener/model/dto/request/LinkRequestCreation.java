package eu.kaesebrot.dev.shortener.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record LinkRequestCreation(
    @NotNull
    @JsonProperty("redirect_uri")
    String redirectUri,

    @JsonProperty("short_uri")
    @Size(min = 5, max = 32)
    @Pattern(regexp = "^[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]$", message = "Id is in wrong format! Has to be 5-32 chars long and only containing lowercase chars, uppercase chars, digits or dashes. Id cannot start or end with a dash.")
    String shortUri
) implements Serializable {

}
