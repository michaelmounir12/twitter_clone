package com.example.twitter.twitter.controller;


import com.example.twitter.twitter.model.Comment;
import com.example.twitter.twitter.responses.ApiResponse;
import com.example.twitter.twitter.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/twitter")
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    ApiResponse apiResponse;

    @PostMapping(path = "/tweet/{tweet_id}/comment", produces = "application/json")
    public ResponseEntity<Object> userMakesCommentAtTweet(
            @RequestBody Comment comment,
            Authentication authentication,
            @PathVariable("tweet_id") long tweet_id
    ) throws Exception{
        Comment savedComment = commentService.userMakesNewCommentAtTweet(authentication, tweet_id, comment);
        apiResponse.setData(savedComment);
        apiResponse.setMessage("Comment crated");
        return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
    }


    @GetMapping(path = "/tweet/{tweet_id}/comment", produces = "application/json")
    public ResponseEntity<Object> showTweetComments(@PathVariable("tweet_id") long tweet_id)
    {
        List<Comment> tweet_comments = commentService.showTweetComments(tweet_id);
        apiResponse.setData(tweet_comments);
        apiResponse.setMessage("tweet comments");
        return new ResponseEntity<>(apiResponse.getBodyResponse(),HttpStatus.OK);
    }

    @DeleteMapping(path="/comment/{comment_id}", produces = "application/json")
    public ResponseEntity<Object> deleteComment(
            Authentication authentication,
            @PathVariable("comment_id")
            long comment_id
    ) throws Exception
    {
        Comment deletedComment = commentService.deleteComment(authentication, comment_id);
        apiResponse.setData(deletedComment);
        apiResponse.setMessage("Comment deleted");
        return new ResponseEntity<>(apiResponse.getBodyResponse(),HttpStatus.OK);
    }


}
