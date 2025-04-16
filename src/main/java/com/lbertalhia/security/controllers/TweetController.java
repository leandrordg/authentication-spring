package com.lbertalhia.security.controllers;

import com.lbertalhia.security.controllers.dtos.CreateTweetDto;
import com.lbertalhia.security.controllers.dtos.FeedResponseDto;
import com.lbertalhia.security.entities.Tweet;
import com.lbertalhia.security.services.TweetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TweetController {

    private final TweetService tweetService;

    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @GetMapping("/tweets")
    public ResponseEntity<FeedResponseDto> getTweets(@RequestParam(value = "page", defaultValue = "0") int page,
                                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        FeedResponseDto response = tweetService.getTweets(page, pageSize);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/tweets")
    public ResponseEntity<Tweet> createTweet(@RequestBody CreateTweetDto dto, JwtAuthenticationToken token) {
        Tweet tweet = tweetService.createTweet(dto, token);
        return ResponseEntity.ok(tweet);
    }

    @DeleteMapping("/tweets/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable("id") Long tweetId, JwtAuthenticationToken token) {
        try {
            tweetService.deleteTweet(tweetId, token);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(e instanceof ResponseStatusException ? ((ResponseStatusException) e).getStatusCode() : HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
