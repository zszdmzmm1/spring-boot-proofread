package com.auefly.spring.boot.security.repository;

import com.auefly.spring.boot.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;



public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findFirstByName(String name);
}
