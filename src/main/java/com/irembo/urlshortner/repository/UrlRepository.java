package com.irembo.urlshortner.repository;

import com.irembo.urlshortner.document.Url;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UrlRepository extends MongoRepository<Url, String> {
    Optional<Url> findByShortUrl(String shortUrl);
    Optional<Url> findByOriginalUrl(String originalUrl);
    List<Url> findAllByExpirationDateTimeBefore(LocalDateTime now);
}
