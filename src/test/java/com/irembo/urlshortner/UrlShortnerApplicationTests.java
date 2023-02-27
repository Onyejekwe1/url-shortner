package com.irembo.urlshortner;

import com.irembo.urlshortner.document.Url;
import com.irembo.urlshortner.service.UrlService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.mongodb.assertions.Assertions.assertNotNull;
import static com.mongodb.assertions.Assertions.assertNull;
import static com.mongodb.internal.connection.tlschannel.util.Util.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UrlShortnerApplicationTests {
	private final UrlService urlService;

	UrlShortnerApplicationTests(UrlService urlService) {
		this.urlService = urlService;
	}

	@Test
	void contextLoads() {
	}

	@Test
	public void testGenerateShortUrl() {
		String shortUrl = urlService.generateShortUrl("https://www.google.com");
		assertNotNull(shortUrl);
		assertTrue(shortUrl.length() > 0);
	}

	@Test
	public void testGetLongUrl() {
		String longUrl = "https://www.google.com";
		String shortUrl = urlService.generateShortUrl(longUrl);
		Url retrievedUrl = urlService.getOriginalUrl(shortUrl);
		assertEquals(longUrl, retrievedUrl.getOriginalUrl());
	}

	@Test
	public void testInvalidUrl() {
		String shortUrl = urlService.generateShortUrl("invalid url");
	}

	@Test
	public void testExpiredUrl() {
		// 1 second expiration
		String longUrl = "https://www.google.com";
		String shortUrl = urlService.generateShortUrl(longUrl);
		try {
			Thread.sleep(2000); // wait for the URL to expire
		} catch (InterruptedException e) {
			// do nothing
		}
		Url retrievedUrl = urlService.getOriginalUrl(shortUrl);
		assertNull(retrievedUrl);
	}

}
