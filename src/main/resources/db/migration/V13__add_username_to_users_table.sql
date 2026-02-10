ALTER TABLE users ADD COLUMN username VARCHAR(100);

UPDATE users
SET username = CONCAT('user_', id);

ALTER TABLE users
MODIFY username VARCHAR(100) NOT NULL UNIQUE;
