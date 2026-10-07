-- Base de datos para la Agenda de Citas

CREATE DATABASE IF NOT EXISTS citas_db;
USE citas_db;

CREATE TABLE IF NOT EXISTS citas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente VARCHAR(100) NOT NULL,
    fecha_hora DATETIME NOT NULL,
    servicio VARCHAR(255) NOT NULL,
    duracion_minutos INT NOT NULL,
    estado ENUM('pendiente', 'confirmada', 'cancelada')
        NOT NULL DEFAULT 'pendiente',

    CONSTRAINT chk_duracion_positiva
        CHECK (duracion_minutos > 0)
);