package com.lbertalhia.security.services;

import com.lbertalhia.security.controllers.dtos.CreateTweetDto;
import com.lbertalhia.security.controllers.dtos.FeedItemDto;
import com.lbertalhia.security.controllers.dtos.FeedResponseDto;
import com.lbertalhia.security.entities.Role;
import com.lbertalhia.security.entities.Tweet;
import com.lbertalhia.security.entities.User;
import com.lbertalhia.security.repositories.TweetRepository;
import com.lbertalhia.security.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    public TweetService(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    public FeedResponseDto getTweets(int page, int pageSize) {
        Page<FeedItemDto> tweets = tweetRepository.findAll(
                        PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationTimestamp"))
                .map(tweet ->
                        new FeedItemDto(
                                tweet.getTweetId(),
                                tweet.getContent(),
                                tweet.getUser().getUsername()
                        )
                );

        return new FeedResponseDto(
                tweets.getContent(),
                page,
                pageSize,
                tweets.getTotalPages(),
                tweets.getTotalElements()
        );
    }

    public Tweet createTweet(CreateTweetDto dto, JwtAuthenticationToken token) {
        User user = getAuthenticatedUser(token);

        Tweet tweet = new Tweet();
        tweet.setUser(user);
        tweet.setContent(dto.content());

        return tweetRepository.save(tweet);
    }

    public void deleteTweet(Long tweetId, JwtAuthenticationToken token) {
        User user = getAuthenticatedUser(token);

        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tweet not found"));

        if (!canDeleteTweet(user, tweet, token)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete this tweet");
        }

        tweetRepository.deleteById(tweetId);
    }

    private User getAuthenticatedUser(JwtAuthenticationToken token) {
        return userRepository.findById(UUID.fromString(token.getName()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    private boolean canDeleteTweet(User user, Tweet tweet, JwtAuthenticationToken token) {
        boolean isAdmin = user.getRoles()
                .stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));

        return isAdmin || tweet.getUser().getUserId().equals(UUID.fromString(token.getName()));
    }
}
