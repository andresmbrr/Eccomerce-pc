CREATE DATABASE IF NOT EXISTS ms_categorias_db;

USE ms_categorias_db;

CREATE TABLE IF NOT EXISTS categorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL UNIQUE,
    descripcion VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL
);

INSERT INTO categorias
(nombre, descripcion, active)
VALUES
('Notebook', 'Computadores portátiles para trabajo, estudio y gaming', true),
('Procesador', 'Procesadores Intel y AMD para computadores de escritorio', true),
('Tarjeta Gráfica', 'GPU para gaming, diseño y alto rendimiento', true),
('Monitor', 'Monitores para oficina, estudio y videojuegos', true);