package eu.kaesebrot.dev.shortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.kaesebrot.dev.shortener.model.EmailConfirmationToken;

@Repository
public interface EmailConfirmationTokenRepository extends JpaRepository<EmailConfirmationToken, String> {

}
