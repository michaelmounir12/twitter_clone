package com.example.twitter.twitter.dao;

import com.example.twitter.twitter.model.Tweet;
import com.example.twitter.twitter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TweetRepository extends JpaRepository<Tweet,Long> {
    @Query("select t from Tweet t where t.user_id=?1 order by t.updatedAt desc")
    List<Tweet> findLatestTweetByUser(Long userid);
    @Query( value="SELECT * "
            + "FROM tweet "
            + "WHERE  user_id IN"
            + "("
            + "SELECT followee FROM follower WHERE follower = ?1"
            + ") "
            + "ORDER BY updatedAt DESC",
            nativeQuery = true
    )
    List<Tweet> findTweetsThatUserFollows(User user);
}
