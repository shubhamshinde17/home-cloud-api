package com.homecloud.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.homecloud.api.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
