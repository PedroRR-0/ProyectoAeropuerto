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
-- Tabla trayectos
-- -----------------------------------------------------
CREATE OR REPLACE TABLE trayectos (
  idTrayecto INT AUTO_INCREMENT,
  destino VARCHAR(45),
  origen VARCHAR(45),
  PRIMARY KEY (idTrayecto)
  );


-- -----------------------------------------------------
-- Tabla miembros
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
  PRIMARY KEY (idTripulacion)
  );



-- -----------------------------------------------------
-- -----------------------------------------------------
-- Tabla pasajeros
-- -----------------------------------------------------
CREATE OR REPLACE TABLE pasajeros (
  idPasajeros INT AUTO_INCREMENT,
  nombre VARCHAR(45),
  apellido1 VARCHAR(45),
  apellido2 VARCHAR(45),
  fechaNacimiento DATE,
  ecorreo VARCHAR(45),
  foto LONGBLOB,
  telefono CHAR(12),
  direccion VARCHAR(45),
  PRIMARY KEY (idPasajeros)
);

-- -----------------------------------------------------
-- Tabla vuelos
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
    -- Hemos restringido el comportamiento de esta FK porque no se pueden borrar aviones
  CONSTRAINT fk_vuelos_trayectos
    FOREIGN KEY (idTrayecto)
    REFERENCES trayectos (idTrayecto)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    -- Hemos restringido el comportamiento de esta FK porque no se pueden borrar trayectos
);

-- -----------------------------------------------------
-- Tabla Pasajeros_vuelos
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
    -- Podemos establecer comportamiento en cascada sin preocuparnos de la propagación ya que esta es una tabla intermedia
  CONSTRAINT fk_vuelos_has_pasajeros_pasajeros
    FOREIGN KEY (idPasajeros)
    REFERENCES pasajeros (idPasajeros)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);




-- -----------------------------------------------------
-- Tabla miembros_vuelos
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
    -- Podemos establecer comportamiento en cascada sin preocuparnos de la propagación ya que esta es una tabla intermedia
    -- Tras consultar con el profesor de programación, hemos decidido que esta tabla será análoga a la tabla Asignados del enunciado
  CONSTRAINT fk_vuelos_has_miembros_miembros
    FOREIGN KEY (idTripulacion)
    REFERENCES miembros (idTripulacion)
    ON DELETE CASCADE
    ON UPDATE CASCADE
	 );


