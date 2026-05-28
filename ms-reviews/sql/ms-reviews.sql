CREATE DATABASE IF NOT EXISTS ms_reviews_db;

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