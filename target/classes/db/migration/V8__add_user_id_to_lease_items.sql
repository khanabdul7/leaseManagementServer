ALTER TABLE lease_items
ADD COLUMN user_id BIGINT;

UPDATE lease_items
SET user_id = 1
WHERE user_id IS NULL;

ALTER TABLE lease_items
MODIFY user_id BIGINT NOT NULL;

ALTER TABLE lease_items
ADD CONSTRAINT fk_lease_items_user
FOREIGN KEY (user_id) REFERENCES users(id);

CREATE INDEX idx_lease_items_user_id ON lease_items(user_id);
