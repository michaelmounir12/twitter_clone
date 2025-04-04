package com.example.twitter.twitter.service;

import com.example.twitter.twitter.dao.TweetRepository;
import com.example.twitter.twitter.dao.UserRepository;
import com.example.twitter.twitter.model.Tweet;
import com.example.twitter.twitter.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class TweetService {
    private final TweetRepository tweetRepository;
    private final UserRepository R_user;
    private final S3Service s3Service;

    public TweetService(TweetRepository tweetRepository, S3Service s3Service, UserRepository R_user) {
        this.tweetRepository = tweetRepository;
        this.s3Service = s3Service;
        this.R_user=R_user;
    }
    public Tweet createTweet(Authentication auth, String text, MultipartFile file) throws Exception{
        if(text==null && file==null) return null;
        String mediaUrl = null;
        if (file != null && !file.isEmpty()) {
            try {
                mediaUrl = s3Service.uploadFile(file);
            } catch (IOException e) {
                throw new IOException("error while saving the tweet");
            }
        }
        User loggedInUser  = R_user.findByUsername(auth.getName());

        Tweet tweet = new Tweet();
        tweet.setText(text);
        tweet.setMediaUrl(mediaUrl);
        tweet.setUser_id(loggedInUser);
        return tweetRepository.save(tweet);
    }
    public Tweet deleteTweet(Authentication authentication, long tweet_id) throws Exception
    {
        Tweet tweetToDelete = tweetRepository.findById(tweet_id).orElse(null);
        if( tweetToDelete == null)
        {
            throw new Exception("Tweet to delete not found!");
        }

        User LoggedInUser = R_user.findByUsername(authentication.getName());
        if(LoggedInUser != tweetToDelete.getUser_id())
        {
            throw new Exception("You have no rights to delete this tweet!");
        }

        tweetRepository.delete(tweetToDelete);

        return tweetToDelete;
    }
    public List<Tweet> showUserTweets(long user_id)
    {
        List<Tweet> userTweets = tweetRepository.findLatestTweetByUser(user_id);

        return userTweets;
    }
    public List<Tweet> getFeed(Authentication auth)
    {
        User LoggedInUser = R_user.findByUsername(auth.getName());
        List<Tweet> followTweets = tweetRepository.findTweetsThatUserFollows(LoggedInUser);

        return followTweets;
    }
    public Tweet getTweet(long tweet_id) throws Exception
    {
        Tweet tweet = tweetRepository.findById(tweet_id).orElse(null);
        if(tweet == null)
        {
            throw new Exception("No tweet found!");
        }
        return tweet;
    }
}
