package de.ksbrt.shortener.security;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import de.ksbrt.shortener.link.Link;

@Entity
public class ShortenerUser {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Id
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank 
    private String role;

    @OneToMany(mappedBy="shorteneruser")
    private Set<Link> links;

    public ShortenerUser(@NotBlank String username, @NotBlank String rawPassword) {
        this.username = username;
        this.password = passwordEncoder.encode(rawPassword);
        this.role = SecurityConfig.USER;
        this.links = null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String rawPassword) {
        this.password = passwordEncoder.encode(rawPassword);
    }

    public Set<Link> getLinks() {
        return links;
    }

    public void setLinks(Set<Link> links) {
        this.links = links;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
}
