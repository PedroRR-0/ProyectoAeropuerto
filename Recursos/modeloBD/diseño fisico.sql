CREATE OR REPLACE DATABASE PROY3TE4;
USE PROY3TE4;

CREATE OR REPLACE TABLE aviones (
  idAvion INT AUTO_INCREMENT,
  numAsientos INT ,
  matricula VARCHAR(6) ,
  estado TINYINT(1) ,
  modelo VARCHAR(50) ,
  PRIMARY KEY (idAvion)
  );


-- -----------------------------------------------------
-- Table trayectos
-- -----------------------------------------------------
CREATE OR REPLACE TABLE trayectos (
  idTrayecto INT AUTO_INCREMENT,
  destino VARCHAR(45) ,
  origen VARCHAR(45) ,
  PRIMARY KEY (idTrayecto)
  );


-- -----------------------------------------------------
-- Table miembros
-- -----------------------------------------------------
CREATE OR REPLACE TABLE miembros (
  idTripulacion INT AUTO_INCREMENT,
  ecorreo VARCHAR(45) ,
  nombre VARCHAR(45) ,
  apellido1 VARCHAR(45) ,
  apellido2 VARCHAR(45) ,
  direccion VARCHAR(45) ,
  categoria VARCHAR(45) ,
  edad INT ,
  telefono CHAR(12) ,
  PRIMARY KEY (idTripulacion)
  );



-- -----------------------------------------------------
-- Table pasajeros
-- -----------------------------------------------------
CREATE OR REPLACE TABLE pasajeros (
  idPasajeros INT AUTO_INCREMENT,
  nombre VARCHAR(45) ,
  apellido1 VARCHAR(45) ,
  apellido2 VARCHAR(45) ,
  edad INT ,
  ecorreo VARCHAR(45) ,
  foto LONGBLOB ,
  telefono CHAR(12) ,
  direccion VARCHAR(45) ,
  PRIMARY KEY (idPasajeros)
  );



-- -----------------------------------------------------
-- Table vuelos
-- -----------------------------------------------------
CREATE OR REPLACE TABLE vuelos (
  idVuelo INT AUTO_INCREMENT,
  fecha DATE ,
  horaSalida TIME ,
  horaLlegada TIME ,
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
  idVuelo INT ,
  idAvion INT ,
  idPasajeros INT ,
  PRIMARY KEY (idVuelo, idAvion, idPasajeros),
  CONSTRAINT fk_vuelos_has_pasajeros_vuelos
    FOREIGN KEY (idVuelo , idAvion)
    REFERENCES vuelos (idVuelo , idAvion)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_vuelos_has_pasajeros_pasajeros
    FOREIGN KEY (idPasajeros)
    REFERENCES pasajeros (idPasajeros)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
	 );



-- -----------------------------------------------------
-- Table miembros_vuelos
-- -----------------------------------------------------
CREATE OR REPLACE TABLE miembros_vuelos (
  idVuelo INT ,
  idAvion INT ,
  idTripulacion INT ,
  PRIMARY KEY (idVuelo, idAvion, idTripulacion),
  CONSTRAINT fk_vuelos_has_miembros_vuelos
    FOREIGN KEY (idVuelo , idAvion)
    REFERENCES vuelos (idVuelo , idAvion)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_vuelos_has_miembros_miembros
    FOREIGN KEY (idTripulacion)
    REFERENCES miembros (idTripulacion)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
	 );



-- -----------------------------------------------------
-- Table proy3te4.usuario
-- -----------------------------------------------------
CREATE OR REPLACE TABLE usuario (
  ID_USUARIO INT AUTO_INCREMENT,
  NOMBRE VARCHAR(50),
  CONTRASENIA VARCHAR(50),
  PRIMARY KEY (ID_USUARIO)
  );

