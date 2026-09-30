CREATE SCHEMA IF NOT EXISTS order_db;

CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    amount NUMERIC(12, 2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE order_items (
    order_id BIGINT NOT NULL,
    sku VARCHAR(100) NOT NULL,
    qty INTEGER NOT NULL,

    PRIMARY KEY (order_id, sku),

    FOREIGN KEY (order_id)
        REFERENCES orders(id)
        ON DELETE CASCADE,

    CHECK (qty > 0)
);