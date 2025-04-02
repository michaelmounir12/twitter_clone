package com.example.twitter.twitter.dao;

import com.example.twitter.twitter.model.Follower;
import com.example.twitter.twitter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follower,Long> {
    @Query("select f from Follower f where f.followee = ?1 and f.follower=?2")
    Optional<Follower> findByFolloweeAndFollower(User followee, User follower);
}
