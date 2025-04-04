package com.example.twitter.twitter.service;

import com.example.twitter.twitter.dao.CommentRepository;
import com.example.twitter.twitter.dao.TweetRepository;
import com.example.twitter.twitter.dao.UserRepository;
import com.example.twitter.twitter.model.Comment;
import com.example.twitter.twitter.model.Tweet;
import com.example.twitter.twitter.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;



@Service
public class CommentService {
    @Autowired
    private TweetRepository R_Tweet;
    @Autowired
    private UserRepository R_User;
    @Autowired
    private CommentRepository R_Comment;


    public Comment userMakesNewCommentAtTweet(Authentication authentication, Long tweet_id, Comment comment ) throws Exception
    {
        Tweet CommentedTweet = R_Tweet.findById(tweet_id).orElse(null);
        if(CommentedTweet == null)
        {
            throw new Exception("Tweet not found!");
        }
        User LoggedInUser = R_User.findByUsername(authentication.getName());
        comment.setComment_tweet_id(CommentedTweet);
        comment.setComment_user_id(LoggedInUser);


        return R_Comment.save(comment);
    }

    public Comment deleteComment(Authentication authentication, Long comment_id) throws Exception
    {
        Comment commentToDelete = R_Comment.findById(comment_id).orElse(null);
        if(commentToDelete == null)
        {
            throw new Exception("Comment not found");
        }
        User LoggedInUser = R_User.findByUsername(authentication.getName());

        if(commentToDelete.getComment_user_id() != LoggedInUser)
        {
            throw new Exception("Not authorized to delete this comment");
        }

        R_Comment.delete(commentToDelete);

        return commentToDelete;
    }


    public List<Comment> showTweetComments(long tweet_id)
    {
        Tweet commented_tweet = R_Tweet.findById(tweet_id).orElse(null);
        return R_Comment.findByTweet_id(commented_tweet);
    }
}