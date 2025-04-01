package com.example.twitter.twitter.controller;

import com.example.twitter.twitter.dao.UserRepository;
import com.example.twitter.twitter.dto.LoginDto;
import com.example.twitter.twitter.model.User;
import com.example.twitter.twitter.responses.ApiResponse;
import com.example.twitter.twitter.service.CustomUserDetailsService;
import com.example.twitter.twitter.service.UserCrudService;
import com.example.twitter.twitter.util.JwtUtil;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitter")
public class AuthController {

    @Autowired
    private UserCrudService userCrudService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtTokenUtil;
    @Autowired
    private ApiResponse apiResponse;
    @Autowired
    private LoginDto loginDto;
    @Autowired
    private UserRepository userRepository;



    @PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> register(@RequestBody User user) throws Exception
    {
        User newUser = userCrudService.createNewUser(user);
        apiResponse.setMessage("User created!");
        apiResponse.setData(newUser);

        return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.CREATED);
    }

    @PostMapping(value = "/authenticate", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> authenticateUser(@RequestBody User user)
    {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        authenticationManager.authenticate(auth);

        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        User LoggedInUser = userRepository.findByUsername(user.getUsername());

        loginDto.setJwt(jwt);
        loginDto.setUsername(user.getUsername());
        loginDto.setUserid(LoggedInUser.getUserid());

        apiResponse.setMessage("Auth Token!");
        apiResponse.setData(loginDto);

        return new ResponseEntity<>(apiResponse.getBodyResponse(),HttpStatus.OK);
    }
}