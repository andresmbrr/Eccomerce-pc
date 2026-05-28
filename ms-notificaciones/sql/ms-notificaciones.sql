CREATE DATABASE IF NOT EXISTS ms_notificaciones_db;

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