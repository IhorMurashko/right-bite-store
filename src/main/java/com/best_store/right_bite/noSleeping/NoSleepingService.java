package com.best_store.right_bite.noSleeping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
public class NoSleepingService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final AtomicReference<Instant> lastLogged = new AtomicReference<>(Instant.EPOCH);


    @Scheduled(fixedRate = 30_000)
    @Async
    public void noSleep() {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity("https://www.google.com", String.class);
            if (shouldLog()) {
                log.info("No sleeping status: {}", responseEntity.getStatusCode());
            }
        } catch (RestClientException ex) {
            log.error(ex.getMessage());
        }
    }

    private boolean shouldLog() {
        Instant now = Instant.now();
        Instant last = lastLogged.get();

        if (Duration.between(last, now).toMinutes() >= 30) {
            lastLogged.set(now);
            return true;
        }

        return false;
    }


}
