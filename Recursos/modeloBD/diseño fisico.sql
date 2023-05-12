CREATE OR REPLACE DATABASE PROY3TE4;
USE PROY3TE4;

CREATE OR REPLACE TABLE usuario (
  ID_USUARIO INT AUTO_INCREMENT,
  NOMBRE VARCHAR(50) UNIQUE,
  CONTRASENIA VARCHAR(50),
  PRIMARY KEY (ID_USUARIO)
  );

CREATE OR REPLACE TABLE aviones (
  idAvion INT AUTO_INCREMENT,
  numAsientos INT,
  matricula VARCHAR(6) UNIQUE,
  estado TINYINT(1),
  modelo VARCHAR(50),
  PRIMARY KEY (idAvion)
  );


-- -----------------------------------------------------
-- Table trayectos
-- -----------------------------------------------------
CREATE OR REPLACE TABLE trayectos (
  idTrayecto INT AUTO_INCREMENT,
  origen VARCHAR(45),
  destino VARCHAR(45),
  PRIMARY KEY (idTrayecto)
  );


-- -----------------------------------------------------
-- Table miembros
-- -----------------------------------------------------
CREATE OR REPLACE TABLE miembros (
  idTripulacion INT AUTO_INCREMENT,
  ecorreo VARCHAR(45) UNIQUE,
  nombre VARCHAR(45),
  apellido1 VARCHAR(45),
  apellido2 VARCHAR(45),
  direccion VARCHAR(45),
  categoria ENUM('azafato', 'piloto', 'copiloto', 'ingeniero de vuelo'),
  -- Hemos añadido una categoría al tipo enum para considerar mecánicos o ingenieros de vuelo
  fechaNacimiento DATE,
  telefono CHAR(12) UNIQUE,
foto LONGBLOB,
  PRIMARY KEY (idTripulacion)
  );



-- -----------------------------------------------------
-- -----------------------------------------------------
-- Table pasajeros
-- -----------------------------------------------------
CREATE OR REPLACE TABLE pasajeros (
  idPasajeros INT AUTO_INCREMENT,
  nombre VARCHAR(45),
  apellido1 VARCHAR(45),
  apellido2 VARCHAR(45),
  fechaNacimiento DATE,
  ecorreo VARCHAR(45),
  foto VARCHAR(200),
  telefono CHAR(12),
  direccion VARCHAR(45),
  dni CHAR(9) UNIQUE,
  PRIMARY KEY (idPasajeros)
);

-- -----------------------------------------------------
-- Table vuelos
-- -----------------------------------------------------
CREATE OR REPLACE TABLE vuelos (
  idVuelo INT AUTO_INCREMENT,
  fecha DATE,
  horaSalida TIME,
  horaLlegada TIME,
  idAvion INT,
  idTrayecto INT,
  PRIMARY KEY (idVuelo, idAvion),
  CONSTRAINT fk_vuelos_aviones
    FOREIGN KEY (idAvion)
    REFERENCES aviones (idAvion)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_vuelos_trayectos
    FOREIGN KEY (idTrayecto)
    REFERENCES trayectos (idTrayecto)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

-- -----------------------------------------------------
-- Table Pasajeros_vuelos
-- -----------------------------------------------------
CREATE OR REPLACE TABLE Pasajeros_vuelos (
  idVuelo INT,
  idAvion INT,
  idPasajeros INT,
  PRIMARY KEY (idVuelo, idAvion, idPasajeros),
  CONSTRAINT fk_vuelos_has_pasajeros_vuelos
    FOREIGN KEY (idVuelo, idAvion)
    REFERENCES vuelos (idVuelo, idAvion)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_vuelos_has_pasajeros_pasajeros
    FOREIGN KEY (idPasajeros)
    REFERENCES pasajeros (idPasajeros)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);




-- -----------------------------------------------------
-- Table miembros_vuelos
-- -----------------------------------------------------
CREATE OR REPLACE TABLE miembros_vuelos (
  idVuelo INT,
  idAvion INT,
  idTripulacion INT,
  PRIMARY KEY (idVuelo, idAvion, idTripulacion),
  CONSTRAINT fk_vuelos_has_miembros_vuelos
    FOREIGN KEY (idVuelo, idAvion)
    REFERENCES vuelos (idVuelo, idAvion)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_vuelos_has_miembros_miembros
    FOREIGN KEY (idTripulacion)
    REFERENCES miembros (idTripulacion)
    ON DELETE CASCADE
    ON UPDATE CASCADE
	 );


DELIMITER $$

create function edad (fecha date)

returns int unsigned

begin

	DECLARE num INT UNSIGNED;

	set num=(TIMESTAMPDIFF( year, fecha, curdate() ));

return num;

end

$$

INSERT INTO miembros (idTripulacion, ecorreo, nombre, apellido1, apellido2, direccion, categoria, fechaNacimiento, telefono) VALUES
    (1, 'juan.perez@mail.com', 'Juan', 'Perez', 'Gonzalez', 'Calle Mayor 1', 'azafato', '1990-01-01', '123456789'),
    (2, 'ana.sanchez@mail.com', 'Ana', 'Sanchez', 'Garcia', 'Plaza Mayor 2', 'piloto', '1985-02-15', '987654321'),
    (3, 'pedro.lopez@mail.com', 'Pedro', 'Lopez', 'Martinez', 'Calle del Sol 3', 'copiloto', '1992-06-03', '654321987'),
    (4, 'maria.rodriguez@mail.com', 'Maria', 'Rodriguez', 'Jimenez', 'Avenida de la Paz 4', 'ingeniero de vuelo', '1988-12-31', '147258369'),
    (5, 'luis.garcia@mail.com', 'Luis', 'Garcia', 'Fernandez', 'Calle del Rio 5', 'azafato', '1995-08-18', '258369147'),
    (6, 'ana.martin@mail.com', 'Ana', 'Martin', 'Lopez', 'Plaza de la Libertad 6', 'piloto', '1990-11-12', '963852741'),
    (7, 'carlos.hernandez@mail.com', 'Carlos', 'Hernandez', 'Gomez', 'Calle del Mar 7', 'copiloto', '1986-03-07', '369147258'),
    (8, 'carmen.sanchez@mail.com', 'Carmen', 'Sanchez', 'Perez', 'Avenida de la Constitucion 8', 'ingeniero de vuelo', '1991-05-22', '741852963'),
    (9, 'antonio.fernandez@mail.com', 'Antonio', 'Fernandez', 'Santos', 'Calle del Pilar 9', 'azafato', '1994-09-28', '852963741'),
    (10, 'laura.garcia@mail.com', 'Laura', 'Garcia', 'Gonzalez', 'Plaza de la Villa 10', 'piloto', '1989-12-24', '369258147'),
    (11, 'daniel.perez@mail.com', 'Daniel', 'Perez', 'Hernandez', 'Calle del Carmen 11', 'copiloto', '1996-04-14', '258147369'),
    (12, 'patricia.martinez@mail.com', 'Patricia', 'Martinez', 'Fernandez', 'Avenida de la Cruz 12', 'ingeniero de vuelo', '1987-07-11', '147369258'),
    (13, 'alberto.sanchez@mail.com', 'Alberto', 'Sanchez', 'Rodriguez', 'Calle del Bosque 13', 'azafato', '1993-10-31', '963741852');