package com.auefly.spring.boot.security;

import com.auefly.spring.boot.security.entity.Permission;
import com.auefly.spring.boot.security.entity.Role;
import com.auefly.spring.boot.security.entity.User;
import com.auefly.spring.boot.security.repository.PermissionRepository;
import com.auefly.spring.boot.security.repository.RoleRepository;
import com.auefly.spring.boot.security.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class JPATest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("java User类与数据库实现对接")
    void UserTableInitTest() {
        User user = new User();
        user.setName("admin");
        user.setPassword(passwordEncoder.encode("secret"));
        //将会返回一个值并赋给user的id属性
        User returnUser = userRepository.save(user);
        Assertions.assertEquals(user, returnUser);

        userRepository.delete(returnUser);
        Assertions.assertEquals(0, userRepository.findAll().size());
    }

    @Test
    @DisplayName("java Role类与数据库实现对接")
    void roleTableInitTest() {
        List<Role> roles = Arrays.asList(
                new Role(0, "admin"),
                new Role(0, "user")
        );

        List<Role> returnRoles = roleRepository.saveAll(roles);
        Assertions.assertEquals(roles, returnRoles);

        roleRepository.deleteAll();
        Assertions.assertEquals(0, roleRepository.findAll().size());
    }

    @Test
    @DisplayName("java Permission类与数据库实现对接")
    void permissionTableInitTest() {
        List<Permission> permissions = Arrays.asList(
                new Permission(0, "/admin/**"),
                new Permission(0, "/login"),
                new Permission(0, "/build/**")
        );

        List<Permission> returnPermission = permissionRepository.saveAll(permissions);
        Assertions.assertEquals(permissions, returnPermission);

        permissionRepository.deleteAll();
        Assertions.assertEquals(0, permissionRepository.findAll().size());
    }
}
