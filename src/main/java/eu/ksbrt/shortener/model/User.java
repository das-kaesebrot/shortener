package eu.ksbrt.shortener.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class User {
    @Id
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
