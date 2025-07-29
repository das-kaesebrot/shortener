package eu.kaesebrot.dev.shortener.repository;

import eu.kaesebrot.dev.shortener.model.AuthUser;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ShortenerUserRepository extends JpaRepository<AuthUser, UUID> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<AuthUser> findByUsername(String username);
    Optional<AuthUser> findByEmail(String email);
}
