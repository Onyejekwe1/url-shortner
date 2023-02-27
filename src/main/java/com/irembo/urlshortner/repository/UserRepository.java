package com.irembo.urlshortner.repository;

import com.irembo.urlshortner.document.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername (String username);
    boolean existsByUsername(String username);
}
