package com.spd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.cdi.JpaRepositoryExtension;
import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension;
import org.springframework.stereotype.Repository;

import com.spd.model.User;
@Repository
public interface UserRepository extends JpaRepository <User, Long> {

    public User findByEmail(String email);
    
} 