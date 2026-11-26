-- Datos de ejemplo de NeroShop (se insertan solo si la base esta vacia).
-- Compatible con SQLite y MySQL.

INSERT INTO categorias (nombre, descripcion) VALUES ('Polos', 'Polos y camisetas de algodon');
INSERT INTO categorias (nombre, descripcion) VALUES ('Camisas', 'Camisas casuales y formales');
INSERT INTO categorias (nombre, descripcion) VALUES ('Pantalones', 'Jeans, joggers y pantalones de vestir');
INSERT INTO categorias (nombre, descripcion) VALUES ('Casacas', 'Casacas, chaquetas y abrigos');
INSERT INTO categorias (nombre, descripcion) VALUES ('Vestidos', 'Vestidos y faldas');
INSERT INTO categorias (nombre, descripcion) VALUES ('Accesorios', 'Gorras, correas, bolsos y mas');

INSERT INTO productos (nombre, descripcion, categoria_id, talla, color, precio, stock, activo)
VALUES ('Polo Oversize Nero', 'Polo oversize 100% algodon', (SELECT id FROM categorias WHERE nombre = 'Polos'), 'M', 'Negro', 49.90, 25, 1);
INSERT INTO productos (nombre, descripcion, categoria_id, talla, color, precio, stock, activo)
VALUES ('Polo Basico', 'Polo cuello redondo', (SELECT id FROM categorias WHERE nombre = 'Polos'), 'L', 'Blanco', 29.90, 40, 1);
INSERT INTO productos (nombre, descripcion, categoria_id, talla, color, precio, stock, activo)
VALUES ('Camisa Oxford', 'Camisa manga larga', (SELECT id FROM categorias WHERE nombre = 'Camisas'), 'M', 'Celeste', 89.90, 15, 1);
INSERT INTO productos (nombre, descripcion, categoria_id, talla, color, precio, stock, activo)
VALUES ('Camisa de Lino', 'Camisa fresca para verano', (SELECT id FROM categorias WHERE nombre = 'Camisas'), 'S', 'Beige', 99.90, 10, 1);
INSERT INTO productos (nombre, descripcion, categoria_id, talla, color, precio, stock, activo)
VALUES ('Jean Slim Fit', 'Jean denim elastizado', (SELECT id FROM categorias WHERE nombre = 'Pantalones'), '32', 'Azul', 119.90, 20, 1);
INSERT INTO productos (nombre, descripcion, categoria_id, talla, color, precio, stock, activo)
VALUES ('Jogger Cargo', 'Jogger con bolsillos laterales', (SELECT id FROM categorias WHERE nombre = 'Pantalones'), 'M', 'Negro', 89.90, 18, 1);
INSERT INTO productos (nombre, descripcion, categoria_id, talla, color, precio, stock, activo)
VALUES ('Casaca Bomber', 'Casaca bomber acolchada', (SELECT id FROM categorias WHERE nombre = 'Casacas'), 'L', 'Verde olivo', 189.90, 8, 1);
INSERT INTO productos (nombre, descripcion, categoria_id, talla, color, precio, stock, activo)
VALUES ('Casaca de Cuero', 'Casaca de cuero sintetico', (SELECT id FROM categorias WHERE nombre = 'Casacas'), 'M', 'Negro', 249.90, 5, 1);
INSERT INTO productos (nombre, descripcion, categoria_id, talla, color, precio, stock, activo)
VALUES ('Vestido Midi', 'Vestido midi satinado', (SELECT id FROM categorias WHERE nombre = 'Vestidos'), 'S', 'Rojo vino', 159.90, 12, 1);
INSERT INTO productos (nombre, descripcion, categoria_id, talla, color, precio, stock, activo)
VALUES ('Falda Plisada', 'Falda plisada a la cintura', (SELECT id FROM categorias WHERE nombre = 'Vestidos'), 'M', 'Negro', 79.90, 14, 1);
INSERT INTO productos (nombre, descripcion, categoria_id, talla, color, precio, stock, activo)
VALUES ('Gorra Nero', 'Gorra bordada con logo', (SELECT id FROM categorias WHERE nombre = 'Accesorios'), 'Unica', 'Negro', 39.90, 30, 1);
INSERT INTO productos (nombre, descripcion, categoria_id, talla, color, precio, stock, activo)
VALUES ('Correa de Cuero', 'Correa con hebilla metalica', (SELECT id FROM categorias WHERE nombre = 'Accesorios'), 'Unica', 'Marron', 59.90, 22, 1);

INSERT INTO clientes (nombres, apellidos, documento, email, telefono, direccion)
VALUES ('Cliente', 'General', '00000000', NULL, NULL, NULL);
INSERT INTO clientes (nombres, apellidos, documento, email, telefono, direccion)
VALUES ('Lucia', 'Ramirez Torres', '45879632', 'lucia.ramirez@correo.com', '987654321', 'Av. Los Olivos 123');
INSERT INTO clientes (nombres, apellidos, documento, email, telefono, direccion)
VALUES ('Carlos', 'Mendoza Diaz', '71234589', 'carlos.mendoza@correo.com', '912345678', 'Jr. Las Flores 456');
