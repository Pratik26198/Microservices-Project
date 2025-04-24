package com.spd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spd.config.JwtProvider;
import com.spd.model.User;
import com.spd.repository.UserRepository;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public User getUserProfile(String jwt) {
      String email = jwtProvider.getEmailFromJwtToken(jwt);
      return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
       return userRepository.findAll();
    }

}
