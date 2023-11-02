package com.auefly.spring.boot.security.service;

import com.auefly.spring.boot.security.bean.SecurityUser;
import com.auefly.spring.boot.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findFirstByName(username)
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found:" + username));
    }
}
