package com.auefly.spring.boot.security.service;

import com.auefly.spring.boot.security.dto.UserDto;
import com.auefly.spring.boot.security.entity.User;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    void updatePassword(User user);
}