package eu.kaesebrot.dev.shortener.security;
import java.util.Set;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

import eu.kaesebrot.dev.shortener.link.Link;

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
        Links = null;
    }

    public ShortenerUser() {}

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
