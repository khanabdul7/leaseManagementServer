ALTER TABLE customers
ADD COLUMN user_id BIGINT;

UPDATE customers
SET user_id = 1
WHERE user_id IS NULL;

ALTER TABLE customers
MODIFY user_id BIGINT NOT NULL;

ALTER TABLE customers
ADD CONSTRAINT fk_customers_user
FOREIGN KEY (user_id) REFERENCES users(id);

CREATE INDEX idx_customers_user_id ON customers(user_id);
