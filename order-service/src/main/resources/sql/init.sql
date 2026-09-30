CREATE SCHEMA IF NOT EXISTS order_db;

CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    amount NUMERIC(12, 2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT orders_status_check CHECK (
        status IN (
            'CREATED',
            'AWAITING_INVENTORY',
            'AWAITING_PAYMENT',
            'CONFIRMED',
            'CANCELLED'
        )
    )
);

CREATE TABLE items (
    id BIGSERIAL PRIMARY KEY,
    sku VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE order_items (
    order_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    qty INTEGER NOT NULL,

    PRIMARY KEY (order_id, item_id),

    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id)
        REFERENCES orders(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_order_items_item
        FOREIGN KEY (item_id)
        REFERENCES items(id),

    CONSTRAINT order_items_qty_check
        CHECK (qty > 0)
);