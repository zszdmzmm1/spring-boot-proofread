ALTER TABLE user ADD COLUMN avatar VARCHAR(512) AFTER phone;
ALTER TABLE user ADD COLUMN social_provider VARCHAR(255) AFTER avatar;
ALTER TABLE user ADD COLUMN social_id VARCHAR(255) AFTER social_provider;