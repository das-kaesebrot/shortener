package eu.kaesebrot.dev.shortener.repository;

import eu.kaesebrot.dev.shortener.model.Link;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LinkRepository extends JpaRepository<Link, String> {
    Page<Link> findLinksByOwnerId(UUID ownerId, Pageable pageable);
}
