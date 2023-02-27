package com.irembo.urlshortner.scheduler;

import com.irembo.urlshortner.service.UrlService;
import org.springframework.scheduling.annotation.Scheduled;

public class ExpiredUrlDeleteScheduler {
    private final UrlService urlService;

    public ExpiredUrlDeleteScheduler(UrlService urlService) {
        this.urlService = urlService;
    }

    @Scheduled(cron = "0 0 0 * * ?") // runs every midnight
    public void deleteExpiredUrls() {
        urlService.deleteExpiredUrls();
    }
}
