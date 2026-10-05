
CREATE SCHEMA IF NOT EXISTS inventory_db;

CREATE TABLE inventory_db.stock (
    sku VARCHAR(100) PRIMARY KEY,
    available_qty INTEGER NOT NULL,
    reserved_qty INTEGER NOT NULL ,

    CHECK (available_qty >= 0),
    CHECK (reserved_qty >= 0)
);

CREATE TABLE inventory_db.processed_orders (
    order_id BIGINT PRIMARY KEY
);

CREATE TABLE inventory_db.reservations (
    order_id BIGINT NOT NULL,
    sku VARCHAR(100) NOT NULL,
    qty INTEGER NOT NULL CHECK (qty > 0),

    PRIMARY KEY (order_id, sku),

    FOREIGN KEY (order_id)
        REFERENCES inventory_db.processed_orders(order_id),

    FOREIGN KEY (sku)
        REFERENCES inventory_db.stock(sku)
);

INSERT INTO inventory_db.stock (sku, available_qty, reserved_qty)
VALUES
    ('PHONE-001', 10, 0),
    ('PHONE-002', 5, 0),
    ('LAPTOP-001', 3, 0),
    ('LAPTOP-002', 2, 0),
    ('MOUSE-001', 25, 0),
    ('KEYBOARD-001', 15, 0),
    ('HEADPHONES-001', 8, 0);