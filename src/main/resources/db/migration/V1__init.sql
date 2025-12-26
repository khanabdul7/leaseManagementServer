CREATE TABLE customers (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(120) NOT NULL,
  phone VARCHAR(20) NOT NULL UNIQUE,
  address VARCHAR(255)
);

CREATE TABLE items (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(120) NOT NULL UNIQUE,
  price_per_day DECIMAL(10,2) NOT NULL
);

CREATE TABLE leases (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  customer_id BIGINT NOT NULL,
  item_id BIGINT NOT NULL,
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  daily_rate_snapshot DECIMAL(10,2) NOT NULL,
  total_days INT NOT NULL,
  total_bill DECIMAL(10,2) NOT NULL,
  notes VARCHAR(255),
  CONSTRAINT fk_leases_customer FOREIGN KEY (customer_id) REFERENCES customers(id),
  CONSTRAINT fk_leases_item FOREIGN KEY (item_id) REFERENCES items(id)
);
