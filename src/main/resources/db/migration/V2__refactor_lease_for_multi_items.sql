-- Step 1: Drop foreign key constraints and old columns from leases
ALTER TABLE leases DROP CONSTRAINT fk_leases_item;

-- Step 2: Drop old columns
ALTER TABLE leases DROP COLUMN item_id;
ALTER TABLE leases DROP COLUMN start_date;
ALTER TABLE leases DROP COLUMN end_date;
ALTER TABLE leases DROP COLUMN daily_rate_snapshot;
ALTER TABLE leases DROP COLUMN total_days;
ALTER TABLE leases DROP COLUMN total_bill;

-- Step 3: Add grand_total column to leases
ALTER TABLE leases
    ADD COLUMN grand_total DECIMAL(10,2) NOT NULL DEFAULT 0 AFTER customer_id;

-- Step 4: Create lease_items table
CREATE TABLE lease_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    lease_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    daily_rate_snapshot DECIMAL(10,2) NOT NULL,
    total_days INT NOT NULL,
    total_bill DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_leaseitems_lease FOREIGN KEY (lease_id) REFERENCES leases(id),
    CONSTRAINT fk_leaseitems_item FOREIGN KEY (item_id) REFERENCES items(id)
);