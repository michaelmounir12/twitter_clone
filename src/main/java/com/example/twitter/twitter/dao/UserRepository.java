package com.example.twitter.twitter.dao;


import com.example.twitter.twitter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String username);
    List<User> findByUsernameNot(String username);
}