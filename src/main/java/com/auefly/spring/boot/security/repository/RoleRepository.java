package com.auefly.spring.boot.security.repository;

import com.auefly.spring.boot.security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
