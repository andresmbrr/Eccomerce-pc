CREATE DATABASE IF NOT EXISTS ms_pagos_db;

USE ms_pagos_db;

CREATE TABLE IF NOT EXISTS payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    payment_method VARCHAR(80) NOT NULL,
    status VARCHAR(30) NOT NULL,
    payment_date DATETIME NOT NULL,
    active BOOLEAN NOT NULL
);

INSERT INTO payments
(order_id, amount, payment_method, status, payment_date, active)
VALUES
(1, 949990, 'DEBIT_CARD', 'APPROVED', NOW(), true),
(2, 129990, 'CREDIT_CARD', 'APPROVED', NOW(), true),
(3, 39990, 'TRANSFER', 'REJECTED', NOW(), true);