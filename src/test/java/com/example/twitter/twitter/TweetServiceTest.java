package com.example.twitter.twitter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.example.twitter.twitter.dao.TweetRepository;
import com.example.twitter.twitter.dao.UserRepository;
import com.example.twitter.twitter.model.Tweet;
import com.example.twitter.twitter.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import com.example.twitter.twitter.service.TweetService;

import io.jsonwebtoken.lang.Assert;

@SpringBootTest
public class TweetServiceTest {

    @InjectMocks
    TweetService service;
    @Mock
    TweetRepository R_Tweet;
    @Mock
    UserRepository R_User;

    @Mock
    Authentication authentication;


    @Test
    void createTweet_should_return_a_tweet() throws Exception {
        User loggedInUser = new User();
        loggedInUser.setUsername("t1user");
        loggedInUser.setUserid(1);
        Tweet newTweet = new Tweet();
        newTweet.setText("unit testing");
        Mockito.when(authentication.getName()).thenReturn("t1user");
        Mockito.when(R_User.findByUsername("t1user")).thenReturn(loggedInUser);
        Mockito.when(R_Tweet.save(any(Tweet.class))).thenReturn(newTweet);

        Tweet savedTweet = service.createTweet(authentication, newTweet.getText(),null);
        Assert.isTrue(savedTweet instanceof Tweet);
        Assert.isTrue("unit testing".equals(newTweet.getText()));
        Assert.isTrue(loggedInUser.equals(newTweet.getUser_id()));
    }



    @Test
        /*
         * Is it a useful test?
         */
    void showTweet_sould_return_a_list_of_tweets()
    {
        User requestedUser = new User();
        requestedUser.setUserid(1);

        Tweet tweet1 = new Tweet();
        tweet1.setText("tweet 1");
        tweet1.setUser_id(requestedUser);

        Tweet tweet2 = new Tweet();
        tweet2.setText("tweet 2");
        tweet2.setUser_id(requestedUser);

        Tweet tweet3 = new Tweet();
        tweet3.setText("tweet 3");
        tweet3.setUser_id(requestedUser);


        List<Tweet> userTweets = new ArrayList<>();
        userTweets.add(tweet1);
        userTweets.add(tweet2);
        userTweets.add(tweet3);

        Mockito.when(R_Tweet.findLatestTweetByUser(1L)).thenReturn(userTweets);

        List<Tweet> tweets = service.showUserTweets(1);
        tweets.get(0);
        assertThat(tweets.get(0)).isEqualTo(tweet1);
    }
}