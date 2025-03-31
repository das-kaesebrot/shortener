package eu.kaesebrot.dev.shortener.repository;

import eu.kaesebrot.dev.shortener.model.ShortenerUser;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ShortenerUserRepository extends JpaRepository<ShortenerUser, UUID> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
