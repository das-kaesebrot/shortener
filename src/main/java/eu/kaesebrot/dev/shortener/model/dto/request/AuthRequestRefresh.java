package eu.kaesebrot.dev.shortener.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

public record AuthRequestRefresh(@NotNull @JsonProperty("user_id") UUID userId, @NotNull @JsonProperty("refresh_token") String refreshToken) implements Serializable {}
