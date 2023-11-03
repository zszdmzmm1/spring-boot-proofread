INSERT INTO `user` (`name`, `password`, `enabled`, `email`, `phone`)
VALUES
    ('visitor','$2a$10$X/uMNuiis.fyO47cxbta3OSs2sllSeLcwVfC0.ghyxeVVZRmAbzk2',b'1','visitor@example.com',NULL);

INSERT INTO `role` (`name`, `description`, `sort`)
VALUES
    ('visitor','游客',0);