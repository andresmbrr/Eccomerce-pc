CREATE DATABASE IF NOT EXISTS ms_auth_db;
CREATE DATABASE IF NOT EXISTS ms_user_db;
CREATE DATABASE IF NOT EXISTS ms_productos_db;
CREATE DATABASE IF NOT EXISTS ms_categorias_db;
CREATE DATABASE IF NOT EXISTS ms_stock_db;
CREATE DATABASE IF NOT EXISTS ms_carrito_db;
CREATE DATABASE IF NOT EXISTS ms_pedidos_db;
CREATE DATABASE IF NOT EXISTS ms_pagos_db;
CREATE DATABASE IF NOT EXISTS ms_reviews_db;
CREATE DATABASE IF NOT EXISTS ms_notificaciones_db;

USE ms_auth_db;

CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(80) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT fk_users_roles
        FOREIGN KEY (role_id) REFERENCES roles(id)
);

INSERT IGNORE INTO roles (name) VALUES
('ADMIN'),
('CLIENTE'),
('OPERADOR');

USE ms_user_db;

CREATE TABLE IF NOT EXISTS user_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    auth_user_id BIGINT NOT NULL UNIQUE,
    first_name VARCHAR(80) NOT NULL,
    last_name VARCHAR(80) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL,
    active BOOLEAN NOT NULL
);

INSERT IGNORE INTO user_profiles
(auth_user_id, first_name, last_name, phone, address, birth_date, active)
VALUES
(1, 'Andres', 'Bustamante', '+56912345678', 'Santiago, Chile', '2000-05-15', true);

USE ms_categorias_db;

CREATE TABLE IF NOT EXISTS categorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL UNIQUE,
    descripcion VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL
);

INSERT IGNORE INTO categorias
(nombre, descripcion, active)
VALUES
('Notebook', 'Computadores portátiles para trabajo, estudio y gaming', true),
('Procesador', 'Procesadores Intel y AMD para computadores de escritorio', true),
('Tarjeta Gráfica', 'GPU para gaming, diseño y alto rendimiento', true),
('Monitor', 'Monitores para oficina, estudio y videojuegos', true),
('Perifericos', 'Teclados, mouse, audífonos y accesorios gamer', true);

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

USE ms_carrito_db;

CREATE TABLE IF NOT EXISTS cart_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(12,2) NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,
    active BOOLEAN NOT NULL
);

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

USE ms_reviews_db;

CREATE TABLE IF NOT EXISTS reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    rating INT NOT NULL,
    comentario VARCHAR(500) NOT NULL,
    fecha DATETIME NOT NULL,
    active BOOLEAN NOT NULL
);

INSERT INTO reviews
(user_id, product_id, rating, comentario, fecha, active)
VALUES
(1, 1, 5, 'Excelente producto, funciona muy bien.', NOW(), true),
(2, 1, 4, 'Buen notebook, aunque el precio es alto.', NOW(), true),
(1, 2, 3, 'Producto aceptable para el precio.', NOW(), true);

USE ms_notificaciones_db;

CREATE TABLE IF NOT EXISTS notificaciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    titulo VARCHAR(120) NOT NULL,
    mensaje VARCHAR(500) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    enviado BOOLEAN NOT NULL,
    fecha_envio DATETIME NOT NULL,
    active BOOLEAN NOT NULL
);

INSERT INTO notificaciones
(user_id, titulo, mensaje, tipo, enviado, fecha_envio, active)
VALUES
(1, 'Pago aprobado', 'Tu pago fue aprobado correctamente', 'EMAIL', true, NOW(), true),
(2, 'Pedido enviado', 'Tu pedido fue enviado a despacho', 'PUSH', true, NOW(), true),
(1, 'Pedido cancelado', 'Tu pedido fue cancelado correctamente', 'SMS', true, NOW(), true);