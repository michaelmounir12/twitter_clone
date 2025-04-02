package com.example.twitter.twitter.service;

import com.example.twitter.twitter.dao.UserRepository;
import com.example.twitter.twitter.dao.FollowRepository;
import com.example.twitter.twitter.model.Follower;
import com.example.twitter.twitter.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class FollowService {
    @Autowired
    private UserRepository R_User;
    @Autowired
    private FollowRepository R_Follower;
    public void followUser(Authentication auth,Long followee_id) throws Exception{

        User follower = R_User.findByUsername(auth.getName());
        User followee = R_User.findById(followee_id).orElse(null);

        if(followee==null) throw new Exception("user not found");
        Follower f = new Follower();
        f.setFollower(follower);
        f.setFollowee(followee);
        R_Follower.save(f);

    }
    public void unfollowUser(Authentication auth,Long followee_id) throws Exception{
        User LoggedInUser = R_User.findByUsername(auth.getName());
        User FolloweeUser = R_User.findById(followee_id).orElse(null);
        if(FolloweeUser == null)
        {
            throw new Exception("User not found!");
        }

        Follower f = R_Follower.findByFolloweeAndFollower(FolloweeUser,LoggedInUser).orElse(null);
        if(f == null)
        {
            throw new Exception("User not following " + FolloweeUser.getUsername());
        }
        R_Follower.delete(f);
    }
}
