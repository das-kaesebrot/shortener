package eu.kaesebrot.dev.shortener.security;

import eu.kaesebrot.dev.shortener.model.ShortenerUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortenerUserRepository extends JpaRepository<ShortenerUser, String> {
    
}
