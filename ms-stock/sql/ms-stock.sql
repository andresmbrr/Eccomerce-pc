CREATE DATABASE IF NOT EXISTS ms_stock_db;

USE ms_stock_db;

CREATE TABLE IF NOT EXISTS stock (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    available BOOLEAN NOT NULL
);

INSERT INTO stock
(product_id, quantity, available)
VALUES
(1, 20, true),
(2, 50, true),
(3, 15, true);