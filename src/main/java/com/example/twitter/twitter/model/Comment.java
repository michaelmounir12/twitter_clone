package com.example.twitter.twitter.model;




import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class Comment {
    @Id
    @GeneratedValue
    private long comment_id;

    private String comment_text;
    @ManyToOne
    private User user_id;
    @ManyToOne
    private Tweet tweet_id;
    @CreationTimestamp
    private LocalDateTime comment_created_at;

    public long getComment_id() {
        return comment_id;
    }
    public void setComment_id(long comment_id) {
        this.comment_id = comment_id;
    }
    public String getComment_text() {
        return comment_text;
    }
    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }
    public User getComment_user_id() {
        return user_id;
    }
    public void setComment_user_id(User comment_user_id) {
        this.user_id = comment_user_id;
    }
    public Tweet getComment_tweet_id() {
        return tweet_id;
    }
    public void setComment_tweet_id(Tweet comment_tweet_id) {
        this.tweet_id = comment_tweet_id;
    }
    public LocalDateTime getComment_created_at() {
        return comment_created_at;
    }
    public void setComment_created_at(LocalDateTime comment_created_at) {
        this.comment_created_at = comment_created_at;
    }



}