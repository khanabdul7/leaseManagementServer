-- 1. Add column (nullable first)
ALTER TABLE items
ADD COLUMN user_id BIGINT;

-- 2. Populate existing rows
UPDATE items
SET user_id = 1
WHERE user_id IS NULL;

-- 3. Enforce NOT NULL (MySQL way)
ALTER TABLE items
MODIFY user_id BIGINT NOT NULL;

-- 4. Add FK
ALTER TABLE items
ADD CONSTRAINT fk_items_user
FOREIGN KEY (user_id) REFERENCES users(id);

-- 5. Index
CREATE INDEX idx_items_user_id ON items(user_id);
