INSERT INTO `user` (`id`, `name`, `password`, `enabled`, `email`, `phone`)
VALUES
    (1,'admin','$2a$10$X/uMNuiis.fyO47cxbta3OSs2sllSeLcwVfC0.ghyxeVVZRmAbzk2',b'1','admin@example.com',NULL),
    (2,'user','$2a$10$X/uMNuiis.fyO47cxbta3OSs2sllSeLcwVfC0.ghyxeVVZRmAbzk2',b'1','user@example.com',NULL);

INSERT INTO `role` (`id`, `name`, `description`, `sort`)
VALUES
    (1,'admin','管理员',0),
    (2,'user','普通用户',0);

INSERT INTO `user_role` (`id`, `role_id`, `user_id`)
VALUES
    (1,1,1),
    (2,2,2);

INSERT INTO `permission` (`id`, `name`, `description`, `sort`)
VALUES
    (1,'Dashboard','/backend/dashboard',0),
    (2,'Empty','/backend/empty',0);

INSERT INTO `role_permission` (`id`, `role_id`, `permission_id`)
VALUES
    (1,1,1),
    (2,1,2);
