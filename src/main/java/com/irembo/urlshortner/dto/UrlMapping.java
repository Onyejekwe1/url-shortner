package com.irembo.urlshortner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlMapping {
    private String shortUrl;

    private String originalUrl;

    private LocalDateTime expirationDateTime;

    private int clicks;

    private String customUrl;
}
