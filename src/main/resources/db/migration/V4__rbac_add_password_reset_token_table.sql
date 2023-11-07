CREATE TABLE `password_reset_token`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT,
    `token`           varchar(255) NOT NULL,
    `expiration_date` datetime(6) NOT NULL,
    `user_id`         bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`token`),
    CONSTRAINT FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);