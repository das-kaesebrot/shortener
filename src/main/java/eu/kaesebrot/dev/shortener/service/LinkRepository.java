package eu.kaesebrot.dev.shortener.service;

import eu.kaesebrot.dev.shortener.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    boolean existsByShortUri(String uri);
    Optional<Link> findByShortUri(String shortURI);
}
