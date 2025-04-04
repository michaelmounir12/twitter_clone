package com.example.twitter.twitter.dao;

import com.example.twitter.twitter.model.Comment;
import com.example.twitter.twitter.model.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findByTweet_id(Tweet tweet);

}
