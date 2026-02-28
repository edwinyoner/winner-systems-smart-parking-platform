SELECT SYS_CONTEXT('USERENV','CON_NAME') AS CONTAINER FROM dual;
-- FREEPDB1
SELECT SYS_CONTEXT('USERENV','CURRENT_SCHEMA') AS SCHEMA FROM dual;
-- SYS
SELECT SYS_CONTEXT('USERENV','CON_NAME') FROM dual;
-- FREEPDB1
SELECT name FROM v$pdbs;


-- Asegurarnos que estamos en el PDB correcto
ALTER SESSION SET CONTAINER = FREEPDB1;

-- Crear usuario (schema)
CREATE USER edwinyoner IDENTIFIED BY edwinyoner;

-- Permisos básicos
GRANT CONNECT, RESOURCE TO edwinyoner;

-- Permisos para crear objetos
GRANT CREATE TABLE, CREATE SEQUENCE, CREATE VIEW TO edwinyoner;

-- (Opcional) espacio ilimitado en tablespace por defecto
ALTER USER edwinyoner QUOTA UNLIMITED ON USERS;
