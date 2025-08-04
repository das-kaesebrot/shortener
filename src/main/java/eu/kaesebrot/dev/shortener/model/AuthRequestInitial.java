package eu.kaesebrot.dev.shortener.model;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record AuthRequestInitial(@NotNull String username, @NotNull String password) implements Serializable {}
