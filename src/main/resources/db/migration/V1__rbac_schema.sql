# user
# ------------------------------------------------------------
CREATE TABLE `user` (
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `name` varchar(255) DEFAULT NULL,
                        `password` varchar(255) DEFAULT NULL,
                        `enabled` bit(1) NOT NULL DEFAULT b'1',
                        `email` varchar(32) DEFAULT NULL COMMENT 'email',
                        `phone` varchar(16) DEFAULT NULL COMMENT '手机号',
                        `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `email` (`email`)
) COMMENT='用户表';

# role
# ------------------------------------------------------------
CREATE TABLE `role` (
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `name` varchar(255) DEFAULT NULL COMMENT '角色名称',
                        `description` varchar(255) DEFAULT NULL COMMENT '角色描述',
                        `sort` int NOT NULL DEFAULT '0' COMMENT '角色顺序',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';

# 转储表 user_role
# ------------------------------------------------------------
CREATE TABLE `user_role` (
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `role_id` bigint NOT NULL,
                             `user_id` bigint NOT NULL,
                             PRIMARY KEY (`id`),
                             KEY `FK55itppkw3i07do3h7qoclqd4k` (`user_id`),
                             KEY `FKa68196081fvovjhkek5m97n3y` (`role_id`),
                             CONSTRAINT `FK55itppkw3i07do3h7qoclqd4k` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
                             CONSTRAINT `FKa68196081fvovjhkek5m97n3y` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户角色关系表';

# permission
# ------------------------------------------------------------
CREATE TABLE `permission` (
                              `id` bigint NOT NULL AUTO_INCREMENT,
                              `name` varchar(255) DEFAULT NULL COMMENT '权限名称',
                              `description` varchar(255) DEFAULT NULL COMMENT '权限描述',
                              `sort` int NOT NULL COMMENT '权限顺序',
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='权限表';

# role_permission
# ------------------------------------------------------------
CREATE TABLE `role_permission` (
                                   `id` bigint NOT NULL AUTO_INCREMENT,
                                   `role_id` bigint NOT NULL,
                                   `permission_id` bigint NOT NULL,
                                   PRIMARY KEY (`id`),
                                   KEY `FKlodb7xh4a2xjv39gc3lsop95n` (`role_id`),
                                   KEY `FKf8yllw1ecvwqy3ehyxawqa1qp` (`permission_id`),
                                   CONSTRAINT `FKf8yllw1ecvwqy3ehyxawqa1qp` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`),
                                   CONSTRAINT `FKlodb7xh4a2xjv39gc3lsop95n` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色权限关系表';