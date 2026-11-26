-- Datos de ejemplo de Nero Thread (se insertan solo si la base esta vacia).
-- Compatible con SQLite y MySQL.

INSERT INTO categorias (nombre, descripcion) VALUES ('Polos', 'Polos y camisetas de algodón');
INSERT INTO categorias (nombre, descripcion) VALUES ('Polerones', 'Hoodies y polerones');
INSERT INTO categorias (nombre, descripcion) VALUES ('Camisas', 'Camisas casuales y overshirts');
INSERT INTO categorias (nombre, descripcion) VALUES ('Pantalones', 'Jeans, joggers y cargos');
INSERT INTO categorias (nombre, descripcion) VALUES ('Casacas', 'Casacas, chaquetas y bombers');
INSERT INTO categorias (nombre, descripcion) VALUES ('Vestidos y faldas', 'Vestidos y faldas');
INSERT INTO categorias (nombre, descripcion) VALUES ('Accesorios', 'Gorras, correas y más');

INSERT INTO productos (nombre, descripcion, categoria_id, tallas, color, precio, stock, imagen, activo)
VALUES ('Polo Oversize Nero', 'Polo de corte oversize en algodón peinado de 220 g. Hombros caídos y logo NT bordado en el pecho.', (SELECT id FROM categorias WHERE nombre = 'Polos'), 'S,M,L,XL', 'Negro', 59.90, 30, 'imagenes/productos/polo-oversize-nero.png', 1);
INSERT INTO productos (nombre, descripcion, categoria_id, tallas, color, precio, stock, imagen, activo)
VALUES ('Polo Gráfico Eyes', 'Edición especial con el estampado de los ojos Nero Thread. Corte oversize y tinta de alta duración.', (SELECT id FROM categorias WHERE nombre = 'Polos'), 'S,M,L,XL', 'Hueso', 69.90, 20, 'imagenes/productos/polo-grafico-eyes.png', 1);
INSERT INTO productos (nombre, descripcion, categoria_id, tallas, color, precio, stock, imagen, activo)
VALUES ('Polo Básico', 'El básico que no puede faltar. Cuello redondo, algodón suave y corte regular.', (SELECT id FROM categorias WHERE nombre = 'Polos'), 'XS,S,M,L,XL', 'Blanco', 34.90, 45, 'imagenes/productos/polo-basico-blanco.png', 1);
INSERT INTO productos (nombre, descripcion, categoria_id, tallas, color, precio, stock, imagen, activo)
VALUES ('Hoodie Nero Thread', 'Polerón con capucha de felpa francesa, bolsillo canguro y estampado frontal NERO THREAD.', (SELECT id FROM categorias WHERE nombre = 'Polerones'), 'S,M,L,XL,XXL', 'Gris jaspeado', 139.90, 15, 'imagenes/productos/hoodie-nero-thread.png', 1);
INSERT INTO productos (nombre, descripcion, categoria_id, tallas, color, precio, stock, imagen, activo)
VALUES ('Camisa Oxford', 'Camisa de manga larga en tela oxford, cuello clásico y bolsillo al pecho.', (SELECT id FROM categorias WHERE nombre = 'Camisas'), 'S,M,L,XL', 'Celeste', 89.90, 15, 'imagenes/productos/camisa-oxford.png', 1);
INSERT INTO productos (nombre, descripcion, categoria_id, tallas, color, precio, stock, imagen, activo)
VALUES ('Camisa de Lino', 'Camisa fresca de lino para días de calor. Caída relajada.', (SELECT id FROM categorias WHERE nombre = 'Camisas'), 'S,M,L', 'Beige', 99.90, 10, 'imagenes/productos/camisa-lino.png', 1);
INSERT INTO productos (nombre, descripcion, categoria_id, tallas, color, precio, stock, imagen, activo)
VALUES ('Jean Slim Fit', 'Jean denim elastizado con lavado medio y desgaste en muslos.', (SELECT id FROM categorias WHERE nombre = 'Pantalones'), '28,30,32,34,36', 'Azul', 119.90, 20, 'imagenes/productos/jean-slim.png', 1);
INSERT INTO productos (nombre, descripcion, categoria_id, tallas, color, precio, stock, imagen, activo)
VALUES ('Jogger Cargo', 'Jogger con bolsillos cargo laterales, pretina elástica y puños ajustados.', (SELECT id FROM categorias WHERE nombre = 'Pantalones'), 'S,M,L,XL', 'Negro', 99.90, 18, 'imagenes/productos/jogger-cargo.png', 1);
INSERT INTO productos (nombre, descripcion, categoria_id, tallas, color, precio, stock, imagen, activo)
VALUES ('Casaca Bomber', 'Bomber acolchada con acabados en rib, cierre metálico y bolsillo utilitario en la manga.', (SELECT id FROM categorias WHERE nombre = 'Casacas'), 'S,M,L,XL', 'Verde olivo', 189.90, 8, 'imagenes/productos/casaca-bomber.png', 1);
INSERT INTO productos (nombre, descripcion, categoria_id, tallas, color, precio, stock, imagen, activo)
VALUES ('Casaca de Cuero', 'Casaca biker de cuero sintético con cierre asimétrico y cinturón.', (SELECT id FROM categorias WHERE nombre = 'Casacas'), 'S,M,L', 'Negro', 249.90, 5, 'imagenes/productos/casaca-cuero.png', 1);
INSERT INTO productos (nombre, descripcion, categoria_id, tallas, color, precio, stock, imagen, activo)
VALUES ('Vestido Midi', 'Vestido midi satinado de tiras finas y falda con vuelo.', (SELECT id FROM categorias WHERE nombre = 'Vestidos y faldas'), 'XS,S,M,L', 'Rojo vino', 159.90, 12, 'imagenes/productos/vestido-midi.png', 1);
INSERT INTO productos (nombre, descripcion, categoria_id, tallas, color, precio, stock, imagen, activo)
VALUES ('Falda Plisada', 'Falda plisada a la cintura con pretina y botón metálico.', (SELECT id FROM categorias WHERE nombre = 'Vestidos y faldas'), 'XS,S,M,L', 'Negro', 79.90, 14, 'imagenes/productos/falda-plisada.png', 1);
INSERT INTO productos (nombre, descripcion, categoria_id, tallas, color, precio, stock, imagen, activo)
VALUES ('Gorra Nero', 'Gorra de seis paneles con logo NT bordado y visera curva.', (SELECT id FROM categorias WHERE nombre = 'Accesorios'), 'Única', 'Negro', 49.90, 30, 'imagenes/productos/gorra-nero.png', 1);
INSERT INTO productos (nombre, descripcion, categoria_id, tallas, color, precio, stock, imagen, activo)
VALUES ('Correa de Cuero', 'Correa de cuero con hebilla metálica plateada.', (SELECT id FROM categorias WHERE nombre = 'Accesorios'), 'Única', 'Marrón', 59.90, 0, 'imagenes/productos/correa-cuero.png', 1);

INSERT INTO clientes (nombres, apellidos, documento, email, telefono, direccion)
VALUES ('Lucía', 'Ramírez Torres', '45879632', 'lucia.ramirez@correo.com', '987654321', 'Av. Los Olivos 123');
INSERT INTO clientes (nombres, apellidos, documento, email, telefono, direccion)
VALUES ('Carlos', 'Mendoza Díaz', '71234589', 'carlos.mendoza@correo.com', '912345678', 'Jr. Las Flores 456');
