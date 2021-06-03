CREATE DATABASE dsdpractica3;
USE dsdpractica3;

CREATE TABLE IF NOT EXISTS libro(
	isbn VARCHAR(20) NOT NULL PRIMARY KEY,
	nombre VARCHAR(100),
	autor VARCHAR(100),
	editorial VARCHAR(100),
	precio FLOAT,
	portada_ruta VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS pedido(
	idPedido INT NOT NULL AUTO_INCREMENT  PRIMARY KEY,
	fecha DATE,
	hora_inicio TIME,
	hora_fin TIME
);

CREATE TABLE IF NOT EXISTS usuario(
	idUsuario INT NOT NULL AUTO_INCREMENT  PRIMARY KEY,
	ip VARCHAR(20),
	puerto int,
	nombre VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS sesion(
	idSesion INT NOT NULL,
	isbn VARCHAR(20),
	idPedido INT,
	FOREIGN KEY (isbn) REFERENCES libro(isbn)  ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (idPedido) REFERENCES pedido(idPedido)  ON DELETE CASCADE ON UPDATE CASCADE,
	PRIMARY KEY (idSesion, isbn)
);

CREATE TABLE IF NOT EXISTS usuariosesion(
	idSU INT NOT NULL AUTO_INCREMENT  PRIMARY KEY,
	idSesion INT,
	idUsuario INT,
	tiempo TIME,
	FOREIGN KEY (idSesion) REFERENCES sesion(idSesion)  ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario)  ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS estadoactual(
	id INT NOT NULL PRIMARY KEY,
	sesion INT
);

INSERT INTO libro VALUES
("9788420665658", "Crimen y Castigo", "Fiódor Dostoyevski", "Editorial Alianza", 319.0, "./imagenes/crimenycastigo.jpg"),
("9786070726873" ,"El Perfume", "Patrick Süskind", "booket", 218.0, "./imagenes/elperfume.jpg"),
("9788408062646","Las Cronicas de Narnia: El Leon, La Bruja y El Armario", "C. S. Lewis", "Destino", 179.0, "./imagenes/narnia1.jpg"),
("9786073126830", "La Ladrona de Libros", "Markus Zusak", "Penguin Random House Grupo Editorial", 322.8, "./imagenes/ladronalibros.jpg"),
("9786076189498", "El Principito", "Antonie De Saint-Expéry", "Advanced Marketing", 47.2, "./imagenes/principito.jpg"),
("9789700767529", "Alicia en el País de las Maravillas: Al Otro Lado del Espejo", "Lewis Carroll", "Alianza", 217.0, "./imagenes/alicia.jpg"),
("9789700763392", "Los Miserables", "Víctor Hugo", "Porrúa", 360.0, "./imagenes/miserables.jpg"),
("9788417517212", "Moby Dick", "Herman Melville", "Sexto Piso", 336.0, "./imagenes/mobydick.jpg");

INSERT INTO estadoactual VALUES (1,1);
