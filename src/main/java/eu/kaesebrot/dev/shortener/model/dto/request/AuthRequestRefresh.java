package eu.kaesebrot.dev.shortener.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record AuthRequestRefresh(@NotNull @JsonProperty("refresh_token") String refreshToken) implements Serializable {}
