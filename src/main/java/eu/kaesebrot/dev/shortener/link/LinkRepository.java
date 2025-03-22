package eu.kaesebrot.dev.shortener.link;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<Link, String> {
    
}
