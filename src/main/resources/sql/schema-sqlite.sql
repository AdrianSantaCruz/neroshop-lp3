-- Esquema de Nero Thread para SQLite (se ejecuta automaticamente)

CREATE TABLE IF NOT EXISTS usuarios (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    username      TEXT    NOT NULL UNIQUE,
    password_hash TEXT    NOT NULL,
    nombre        TEXT    NOT NULL,
    rol           TEXT    NOT NULL DEFAULT 'CLIENTE'
);

CREATE TABLE IF NOT EXISTS categorias (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre      TEXT    NOT NULL UNIQUE,
    descripcion TEXT
);

CREATE TABLE IF NOT EXISTS productos (
    id           INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre       TEXT    NOT NULL,
    descripcion  TEXT,
    categoria_id INTEGER NOT NULL REFERENCES categorias(id),
    tallas       TEXT    NOT NULL,
    color        TEXT    NOT NULL,
    precio       REAL    NOT NULL CHECK (precio >= 0),
    stock        INTEGER NOT NULL DEFAULT 0 CHECK (stock >= 0),
    imagen       TEXT,
    activo       INTEGER NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS clientes (
    id         INTEGER PRIMARY KEY AUTOINCREMENT,
    usuario_id INTEGER UNIQUE REFERENCES usuarios(id),
    nombres    TEXT NOT NULL,
    apellidos  TEXT NOT NULL,
    documento  TEXT NOT NULL UNIQUE,
    email      TEXT,
    telefono   TEXT,
    direccion  TEXT
);

CREATE TABLE IF NOT EXISTS pedidos (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    cliente_id      INTEGER NOT NULL REFERENCES clientes(id),
    fecha           TEXT    NOT NULL,
    subtotal        REAL    NOT NULL,
    envio           REAL    NOT NULL,
    total           REAL    NOT NULL,
    estado          TEXT    NOT NULL,
    metodo_pago     TEXT    NOT NULL,
    direccion_envio TEXT    NOT NULL,
    telefono        TEXT
);

CREATE TABLE IF NOT EXISTS detalle_pedido (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    pedido_id       INTEGER NOT NULL REFERENCES pedidos(id) ON DELETE CASCADE,
    producto_id     INTEGER NOT NULL REFERENCES productos(id),
    talla           TEXT    NOT NULL,
    cantidad        INTEGER NOT NULL CHECK (cantidad > 0),
    precio_unitario REAL    NOT NULL,
    subtotal        REAL    NOT NULL
);
