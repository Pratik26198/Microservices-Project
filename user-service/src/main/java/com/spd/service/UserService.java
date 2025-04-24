package com.spd.service;

import java.util.List;

import com.spd.model.User;

public interface UserService {

    public User getUserProfile(String jwt);
    public List<User> getAllUsers();
}
