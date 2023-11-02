package com.auefly.spring.boot.security.repository;

import com.auefly.spring.boot.security.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
}
