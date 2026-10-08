CREATE SCHEMA IF NOT EXISTS payment_db;

CREATE TABLE payment_db.payments (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL UNIQUE,
    amount NUMERIC(12, 2) NOT NULL CHECK (amount > 0),
    status VARCHAR(30) NOT NULL
);
