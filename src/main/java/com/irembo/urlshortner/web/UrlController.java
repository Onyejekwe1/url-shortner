package com.irembo.urlshortner.web;

import com.irembo.urlshortner.document.Url;
import com.irembo.urlshortner.dto.UrlMapping;
import com.irembo.urlshortner.service.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("http://localhost:4200")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/urls")
    public ResponseEntity<UrlMapping> createShortUrl(@Valid @RequestBody UrlMapping urlMapping) {
        urlMapping.setExpirationDateTime(LocalDateTime.now().plusDays(1));
        Url createdUrl = urlService.createShortUrl(urlMapping.getOriginalUrl(), urlMapping.getExpirationDateTime(), urlMapping.getCustomUrl());
        UrlMapping createdUrlMapping = mapToUrlMapping(createdUrl);
        return ResponseEntity.ok().body(createdUrlMapping);
    }

    @GetMapping("/urls/{shortUrl}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortUrl, HttpServletResponse response) throws IOException {
        Url url = urlService.getOriginalUrl(shortUrl);
        if (url != null) {
            urlService.incrementClicks(shortUrl);
            response.sendRedirect(url.getOriginalUrl());
            return ResponseEntity.status(HttpStatus.FOUND).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/urls/original/{shortUrl}")
    public ResponseEntity<Url> getOriginalUrl(@PathVariable String shortUrl) {
        Url url = urlService.getOriginalUrl(shortUrl);
        if (url != null) {
            urlService.incrementClicks(shortUrl);
            return ResponseEntity.ok().body(url);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/urls")
    public ResponseEntity<List<UrlMapping>> getAllUrls() {
        List<Url> urls = urlService.getAllUrls();
        List<UrlMapping> urlMappings = urls.stream().map(this::mapToUrlMapping).collect(Collectors.toList());
        return ResponseEntity.ok().body(urlMappings);
    }

    private UrlMapping mapToUrlMapping(Url url) {
        return new UrlMapping(url.getId(), url.getShortUrl(), url.getOriginalUrl(), url.getExpirationDateTime(), url.getClicks(), url.getCustomUrl());
    }

}