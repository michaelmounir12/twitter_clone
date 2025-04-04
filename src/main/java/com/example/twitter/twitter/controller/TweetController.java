package com.example.twitter.twitter.controller;

import com.example.twitter.twitter.dao.TweetRepository;
import com.example.twitter.twitter.dto.TweetDto;
import com.example.twitter.twitter.model.Tweet;
import com.example.twitter.twitter.model.User;
import com.example.twitter.twitter.responses.ApiResponse;
import com.example.twitter.twitter.service.S3Service;
import com.example.twitter.twitter.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/twitter")
public class TweetController {


    @Autowired
    private ApiResponse apiResponse;
    @Autowired
    private TweetService tweetService;

    @PostMapping(path = "/tweet/create", produces = "application/json")
    public ResponseEntity<Object> createTweet(Authentication auth, @RequestParam("text") String text,
                                              @RequestParam(value = "file", required = false) MultipartFile file) throws Exception {

        Tweet tweet = tweetService.createTweet(auth,text,file);


        apiResponse.setData(tweet);
        apiResponse.setMessage("tweet created successfully");


        return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.CREATED);
    }
    @GetMapping(path = "/tweet/{tweet_id}", produces = "application/json")
    public ResponseEntity<Object> getTweet(@PathVariable("tweet_id") long tweet_id) throws Exception
    {
        Tweet tweet = tweetService.getTweet(tweet_id);
        apiResponse.setMessage("Tweet");
        apiResponse.setData(tweet);

        return new ResponseEntity<>(apiResponse.getBodyResponse(),HttpStatus.OK);
    }
    @DeleteMapping(path = "tweet/{tweet_id}")
    public ResponseEntity<Object> delete(
            @PathVariable("tweet_id") long tweet_id,
            Authentication authentication
    ) throws Exception
    {
        Tweet deletedTweet = tweetService.deleteTweet(authentication, tweet_id);
        apiResponse.setMessage("Tweet deleted!");
        apiResponse.setData(deletedTweet);

        return new ResponseEntity<>(apiResponse.getBodyResponse(),HttpStatus.OK);
    }
    @GetMapping(path = "/tweet/feed", produces = "application/json")
    public ResponseEntity<Object> getFeed(Authentication authentication)
    {

        List<Tweet> userFeed = tweetService.getFeed(authentication);
        apiResponse.setMessage("User Feed!");
        apiResponse.setData(userFeed);

        return new ResponseEntity<>(apiResponse.getBodyResponse(),HttpStatus.OK);
    }

    @GetMapping(path = "/tweet/user/{user_id}", produces = "application/json")
    public ResponseEntity<Object> showTweetsByUser(@PathVariable("user_id") long user_id)
    {
        List<Tweet> userTweets = tweetService.showUserTweets(user_id);
        apiResponse.setMessage("Tweets by user");
        apiResponse.setData(userTweets);

        return new ResponseEntity<>(apiResponse.getBodyResponse(),HttpStatus.OK);
    }
}
