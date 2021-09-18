package de.ksbrt.shortener.security;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortenerUserRepository extends JpaRepository<ShortenerUser, String> {
    
}
