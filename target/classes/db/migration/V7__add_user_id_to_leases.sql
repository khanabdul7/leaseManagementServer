ALTER TABLE leases
ADD COLUMN user_id BIGINT;

UPDATE leases
SET user_id = 1
WHERE user_id IS NULL;

ALTER TABLE leases
MODIFY user_id BIGINT NOT NULL;

ALTER TABLE leases
ADD CONSTRAINT fk_leases_user
FOREIGN KEY (user_id) REFERENCES users(id);

CREATE INDEX idx_leases_user_id ON leases(user_id);
