package eu.kaesebrot.dev.shortener.model.dto.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AuthUserCreationRequest(
        @NotNull
        @Size(min = 5, max = 32)
        @Pattern(regexp = "^[a-z0-9-]+$", message = "Username is in wrong format! Has to be 5-32 chars long and only containing lowercase chars, digits or dashes.")
        @JsonProperty("username")
        String username,
        @NotNull
        @JsonProperty("password")
        String rawPassword,
        @NotNull
        @JsonProperty("email")
        @Email
        String email
) implements Serializable {
}
