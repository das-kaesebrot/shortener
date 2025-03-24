package eu.kaesebrot.dev.shortener.repository;

import eu.kaesebrot.dev.shortener.model.Link;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository extends JpaRepository<Link, String> {
}
