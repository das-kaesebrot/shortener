package eu.kaesebrot.dev.shortener.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseBase implements Serializable {
    @NotNull
    @JsonProperty("jwt")
    private String jwt;

    @NotNull
    @JsonProperty("principal_name")
    private String principalName;

    @NotNull
    @JsonProperty("expires_at")
    private Long expiresAt;
}
