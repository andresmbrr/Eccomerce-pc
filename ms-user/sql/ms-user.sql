CREATE DATABASE IF NOT EXISTS ms_user_db;

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

INSERT INTO user_profiles
(auth_user_id, first_name, last_name, phone, address, birth_date, active)
VALUES
(1, 'Andres', 'Bustamante', '+56912345678', 'Santiago, Chile', '2000-05-15', true);