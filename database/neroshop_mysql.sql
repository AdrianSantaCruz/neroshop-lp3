-- =============================================================
--  Nero Thread - Script de base de datos para MySQL 8
--  Ejecutar en MySQL Workbench (o: mysql -u root -p < neroshop_mysql.sql)
--  Los datos de ejemplo y los usuarios los inserta la aplicacion
--  automaticamente al iniciar por primera vez.
-- =============================================================

CREATE DATABASE IF NOT EXISTS nerothread CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE nerothread;

CREATE TABLE IF NOT EXISTS usuarios (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    password_hash VARCHAR(64)  NOT NULL,
    nombre        VARCHAR(100) NOT NULL,
    rol           VARCHAR(20)  NOT NULL DEFAULT 'CLIENTE'
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS categorias (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(60)  NOT NULL UNIQUE,
    descripcion VARCHAR(255)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS productos (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    nombre       VARCHAR(100)  NOT NULL,
    descripcion  VARCHAR(500),
    categoria_id INT           NOT NULL,
    tallas       VARCHAR(100)  NOT NULL,
    color        VARCHAR(40)   NOT NULL,
    precio       DECIMAL(10,2) NOT NULL CHECK (precio >= 0),
    stock        INT           NOT NULL DEFAULT 0 CHECK (stock >= 0),
    imagen       VARCHAR(255),
    activo       TINYINT(1)    NOT NULL DEFAULT 1,
    CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS clientes (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT UNIQUE,
    nombres    VARCHAR(80)  NOT NULL,
    apellidos  VARCHAR(80)  NOT NULL,
    documento  VARCHAR(20)  NOT NULL UNIQUE,
    email      VARCHAR(100),
    telefono   VARCHAR(20),
    direccion  VARCHAR(150),
    CONSTRAINT fk_cliente_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS pedidos (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id      INT           NOT NULL,
    fecha           DATETIME      NOT NULL,
    subtotal        DECIMAL(10,2) NOT NULL,
    envio           DECIMAL(10,2) NOT NULL,
    total           DECIMAL(10,2) NOT NULL,
    estado          VARCHAR(20)   NOT NULL,
    metodo_pago     VARCHAR(40)   NOT NULL,
    direccion_envio VARCHAR(200)  NOT NULL,
    telefono        VARCHAR(20),
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS detalle_pedido (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id       INT           NOT NULL,
    producto_id     INT           NOT NULL,
    talla           VARCHAR(10)   NOT NULL,
    cantidad        INT           NOT NULL CHECK (cantidad > 0),
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal        DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_detalle_pedido   FOREIGN KEY (pedido_id)   REFERENCES pedidos(id) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_id) REFERENCES productos(id)
) ENGINE = InnoDB;
