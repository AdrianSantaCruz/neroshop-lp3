-- =============================================================
--  NeroShop - Script de base de datos para MySQL 8
--  Ejecutar en MySQL Workbench (o: mysql -u root -p < neroshop_mysql.sql)
--  Los datos de ejemplo y el usuario admin los inserta la aplicacion
--  automaticamente al iniciar por primera vez.
-- =============================================================

CREATE DATABASE IF NOT EXISTS neroshop CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE neroshop;

CREATE TABLE IF NOT EXISTS usuarios (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    password_hash VARCHAR(64)  NOT NULL,
    nombre        VARCHAR(100) NOT NULL,
    rol           VARCHAR(20)  NOT NULL DEFAULT 'VENDEDOR'
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS categorias (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(60)  NOT NULL UNIQUE,
    descripcion VARCHAR(255)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS productos (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    nombre       VARCHAR(100)  NOT NULL,
    descripcion  VARCHAR(255),
    categoria_id INT           NOT NULL,
    talla        VARCHAR(10)   NOT NULL,
    color        VARCHAR(40)   NOT NULL,
    precio       DECIMAL(10,2) NOT NULL CHECK (precio >= 0),
    stock        INT           NOT NULL DEFAULT 0 CHECK (stock >= 0),
    activo       TINYINT(1)    NOT NULL DEFAULT 1,
    CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS clientes (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    nombres   VARCHAR(80)  NOT NULL,
    apellidos VARCHAR(80)  NOT NULL,
    documento VARCHAR(20)  NOT NULL UNIQUE,
    email     VARCHAR(100),
    telefono  VARCHAR(20),
    direccion VARCHAR(150)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS ventas (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT           NOT NULL,
    usuario_id INT           NOT NULL,
    fecha      DATETIME      NOT NULL,
    total      DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_venta_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    CONSTRAINT fk_venta_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS detalle_venta (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    venta_id        INT           NOT NULL,
    producto_id     INT           NOT NULL,
    cantidad        INT           NOT NULL CHECK (cantidad > 0),
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal        DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_detalle_venta    FOREIGN KEY (venta_id)    REFERENCES ventas(id) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_id) REFERENCES productos(id)
) ENGINE = InnoDB;
