CREATE DATABASE IF NOT EXISTS ms_pedidos_db;

USE ms_pedidos_db;

CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    order_date DATETIME NOT NULL,
    total DECIMAL(12,2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    active BOOLEAN NOT NULL
);

INSERT INTO orders
(user_id, order_date, total, status, active)
VALUES
(1, NOW(), 949990, 'PENDING', true),
(2, NOW(), 129990, 'PAID', true),
(1, NOW(), 39990, 'CANCELLED', true);