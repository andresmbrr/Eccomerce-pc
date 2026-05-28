CREATE DATABASE IF NOT EXISTS ms_productos_db;

USE ms_productos_db;

CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    description VARCHAR(255) NOT NULL,
    price DECIMAL(12,2) NOT NULL,
    category VARCHAR(80) NOT NULL,
    active BOOLEAN NOT NULL
);

INSERT INTO products (name, description, price, category, active) VALUES
('Notebook Lenovo Legion 5', 'Notebook gamer con 16GB RAM y SSD', 949990, 'Notebook', true),
('Mouse Logitech G203', 'Mouse gamer RGB para computador', 19990, 'Perifericos', true),
('Monitor Samsung 24', 'Monitor Full HD de 24 pulgadas', 129990, 'Monitor', true);