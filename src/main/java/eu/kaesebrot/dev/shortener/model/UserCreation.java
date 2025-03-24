package eu.kaesebrot.dev.shortener.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserCreation implements Serializable {
    @NotNull
    @Size(min = 5, max = 32)
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Username is in wrong format! Has to be 5-32 chars long and only containing lowercase chars, digits or dashes.")
    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String rawPassword;

    @JsonProperty("email")
    @Email
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRawPassword() {
        return rawPassword;
    }

    public void setRawPassword(String rawPassword) {
        this.rawPassword = rawPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    
}
