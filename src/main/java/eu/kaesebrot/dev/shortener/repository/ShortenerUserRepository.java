package eu.kaesebrot.dev.shortener.repository;

import eu.kaesebrot.dev.shortener.model.ShortenerUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ShortenerUserRepository extends JpaRepository<ShortenerUser, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
