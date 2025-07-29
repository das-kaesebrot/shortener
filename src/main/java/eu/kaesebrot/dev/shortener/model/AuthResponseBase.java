package eu.kaesebrot.dev.shortener.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

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

    public AuthResponseBase() {}

    public AuthResponseBase(String jwt, String principalName, Long expiresAt) {
        this.jwt = jwt;
        this.principalName = principalName;
        this.expiresAt = expiresAt;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }
}
