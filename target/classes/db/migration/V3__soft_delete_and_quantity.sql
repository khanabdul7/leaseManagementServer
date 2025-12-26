-- Customers
ALTER TABLE customers ADD COLUMN is_deleted BOOLEAN DEFAULT FALSE;

-- Items
ALTER TABLE items ADD COLUMN is_deleted BOOLEAN DEFAULT FALSE;

-- Leases
ALTER TABLE leases ADD COLUMN is_deleted BOOLEAN DEFAULT FALSE;

-- Lease Items
ALTER TABLE lease_items ADD COLUMN is_deleted BOOLEAN DEFAULT FALSE;

-- Add Quantity
ALTER TABLE lease_items ADD COLUMN quantity INT DEFAULT 1;
