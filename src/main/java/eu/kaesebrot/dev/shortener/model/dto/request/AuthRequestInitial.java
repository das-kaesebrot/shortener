package eu.kaesebrot.dev.shortener.model.dto.request;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record AuthRequestInitial(@NotNull String username, @NotNull String password) implements Serializable {}
