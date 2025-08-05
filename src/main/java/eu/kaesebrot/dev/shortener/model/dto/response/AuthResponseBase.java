package eu.kaesebrot.dev.shortener.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseBase implements Serializable {
    @NotNull
    @JsonProperty("jwt")
    private String jwt;

    @NotNull
    @JsonProperty("principal")
    private UUID principal;

    @NotNull
    @JsonProperty("expires_at")
    private Long expiresAt;
}
