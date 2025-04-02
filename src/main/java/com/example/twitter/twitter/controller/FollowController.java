package com.example.twitter.twitter.controller;

import com.example.twitter.twitter.responses.ApiResponse;
import com.example.twitter.twitter.service.FollowService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/twitter")
public class FollowController {
    @Autowired
    private ApiResponse apiResponse;
    @Autowired
    FollowService followService;

    @PostMapping(path = "follow/user/{user_id}",produces = "application/json")
    public ResponseEntity<Object> addFollowee(Authentication auth, @PathVariable("user_id") Long user_id)throws Exception{
        followService.followUser(auth,user_id);
        apiResponse.setMessage("followee added");
        apiResponse.setData(user_id);
        return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.CREATED);

    }
    @DeleteMapping(path = "unfollow/user/{user_id}", produces = "application/json")
    public ResponseEntity<Object> deleteFollowee(
            Authentication authentication,
            @PathVariable("user_id") long user_id
    ) throws Exception
    {
        System.out.println("Authentication: " + authentication);

        followService.unfollowUser(authentication, user_id);
        apiResponse.setMessage("Followee removed!");
        apiResponse.setData(user_id);

        return new ResponseEntity<>(apiResponse.getBodyResponse(),HttpStatus.CREATED);
    }
}
