package com.irembo.urlshortner.service;

import com.irembo.urlshortner.document.Url;
import com.irembo.urlshortner.dto.UrlMapping;
import com.irembo.urlshortner.repository.UrlRepository;
import com.irembo.urlshortner.utils.Base62Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlService {
    private final UrlRepository urlRepository;
    SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(1L, 1L);

    private static final String BASE_URL = "https://example.com/";
    private static final int SHORT_URL_LENGTH = 8;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public Url createShortUrl(String originalUrl, LocalDateTime expirationDateTime, String customUrl) {

        // If we have shortened this url in the past, then just return it.
        Optional<Url> existingOriginalUrl = urlRepository.findByOriginalUrl(originalUrl);
        if (existingOriginalUrl.isPresent()){
            return existingOriginalUrl.get();
        }

        String shortUrl = "";
        if (customUrl != null) {
            // Check if the custom ID is already taken
            Optional<Url> existingMapping = urlRepository.findByShortUrl(customUrl);
            if (existingMapping.isPresent()) {
                // Custom Url Already Taken
                // Generate a unique ID for the short URL
                shortUrl = generateShortUrl(originalUrl);
            } else {
                shortUrl = customUrl;
            }

        } else {
            shortUrl = generateShortUrl(originalUrl);
        }

        Url url = new Url(originalUrl, shortUrl, expirationDateTime);
        return urlRepository.save(url);
    }

    public Url getOriginalUrl(String shortUrl) {
        Optional<Url> urlOptional = urlRepository.findByShortUrl(shortUrl);
        return urlOptional.orElse(null);
    }

    public List<Url> getAllUrls() {
        return urlRepository.findAll();
    }

    public void incrementClicks(String shortUrl) {
        Optional<Url> urlOptional = urlRepository.findByShortUrl(shortUrl);
        if (urlOptional.isPresent()) {
            Url url = urlOptional.get();
            url.setClicks(url.getClicks() + 1);
            urlRepository.save(url);
        }
    }

    public void deleteExpiredUrls() {
        LocalDateTime now = LocalDateTime.now();
        List<Url> expiredUrls = urlRepository.findAllByExpirationDateTimeBefore(now);


        urlRepository.deleteAll(expiredUrls);
    }

    public String generateShortUrl(String originalUrl) {
        String shortUrl = null;

        // Generate a unique ID using SnowflakeIdGenerator
        long id = idGenerator.nextId();

        // Convert the ID to Base62 format
        String base62 = Base62Utils.fromBase10(id);

        // Truncate the Base62 string to the desired length
        if (base62.length() > SHORT_URL_LENGTH) {
            base62 = base62.substring(0, SHORT_URL_LENGTH);
        }

        // Combine the Base URL and the short URL to create the final URL
        shortUrl = base62;

        // Check if the short URL is already in use
        Optional<Url> existingUrl = urlRepository.findByShortUrl(shortUrl);
        if (existingUrl.isPresent()) {
            // If the short URL is already in use, recursively generate a new one
            shortUrl = generateShortUrl(originalUrl);
        }

        return shortUrl;
    }


}
