-- Modificación de la tabla citas
-- Agrega la fecha de la última cita del cliente.
-- Este campo es opcional, por eso permite NULL.

USE citas_db;

ALTER TABLE citas
ADD COLUMN fecha_ultima_cita DATE NULL;