package de.ksbrt.shortener.security;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import de.ksbrt.shortener.link.Link;

@Entity
public class ShortenerUser {

    /*
    @Autowired
    @Transient
    private BCryptPasswordEncoder passwordEncoder;
    */    

    @Id
    @NotBlank
    private String Username;

    @NotBlank
    private String Password;

    @NotBlank 
    private String Role;

    @OneToMany(mappedBy="Owner")
    private Set<Link> Links;

    public ShortenerUser(@NotBlank String username, @NotBlank String rawPassword) {
        Username = username;
        Password = rawPassword;
        Role = SecurityConfig.USER;
        Links = null;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String rawPassword) {
        Password = rawPassword;
    }

    public Set<Link> getLinks() {
        return Links;
    }

    public void setLinks(Set<Link> links) {
        Links = links;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }
    
}
