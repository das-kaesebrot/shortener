package eu.kaesebrot.dev.shortener.repository;

import eu.kaesebrot.dev.shortener.model.AuthUser;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, UUID> {
    boolean existsByUsernameOrEmail(String username, String email);
    Optional<AuthUser> findByUsername(String username);
    Optional<AuthUser> findByUsernameOrEmail(String username, String email);
    Long removeByUsername(String username);
    Long removeById(UUID id);
}
