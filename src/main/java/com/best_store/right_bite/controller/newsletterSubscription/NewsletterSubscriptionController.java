package com.best_store.right_bite.controller.newsletterSubscription;

import com.best_store.right_bite.dto.notification.request.NotificationSubscriptionRequestDto;
import com.best_store.right_bite.service.newsLetter.application.NewsLetterSubscriptionFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/newsletter")
@RequiredArgsConstructor
@PreAuthorize("permitAll()")
public class NewsletterSubscriptionController {

    private final NewsLetterSubscriptionFacadeService newsLetterSubscriptionFacadeService;

    @PostMapping("/subscribe")
    public ResponseEntity<HttpStatus> subscribe(@RequestBody NotificationSubscriptionRequestDto notificationSubscriptionRequestDto) {
        newsLetterSubscriptionFacadeService.subscribe(notificationSubscriptionRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<HttpStatus> unsubscribe(@RequestBody NotificationSubscriptionRequestDto notificationSubscriptionRequestDto) {
        newsLetterSubscriptionFacadeService.unsubscribe(notificationSubscriptionRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
