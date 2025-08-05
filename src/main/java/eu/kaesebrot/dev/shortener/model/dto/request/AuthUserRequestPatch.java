package eu.kaesebrot.dev.shortener.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record AuthUserRequestPatch(
        @Nullable
        @Size(min = 5, max = 32)
        @Pattern(regexp = "^[a-z0-9-]+$", message = "Username is in wrong format! Has to be 5-32 chars long and only containing lowercase chars, digits or dashes.")
        @JsonProperty("username")
        String username,

        @Nullable
        @JsonProperty("password")
        String rawPassword,

        @Nullable
        @Email
        @JsonProperty("email")
        String email
) implements Serializable {
}
