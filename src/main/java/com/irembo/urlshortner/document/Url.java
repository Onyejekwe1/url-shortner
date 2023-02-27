package com.irembo.urlshortner.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "urls")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Url {
    @Id
    private String id;

    @Indexed(unique = true)
    private String shortUrl;

    private String originalUrl;

    private LocalDateTime expirationDateTime;

    private int clicks;

    private String customUrl;

    public Url(String originalUrl, String shortUrl, LocalDateTime expirationDateTime) {
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.expirationDateTime = expirationDateTime;
    }
}

