package com.lbertalhia.security.controllers;

import com.lbertalhia.security.dtos.CreateTweetDto;
import com.lbertalhia.security.dtos.FeedResponseDto;
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
        return tweetService.getTweets(page, pageSize);
    }

    @PostMapping("/tweets")
    public ResponseEntity<Tweet> createTweet(@RequestBody CreateTweetDto dto, JwtAuthenticationToken token) {
        return tweetService.createTweet(dto, token);
    }

    @DeleteMapping("/tweets/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable("id") Long tweetId, JwtAuthenticationToken token) {
        return tweetService.deleteTweet(tweetId, token);
    }
}
