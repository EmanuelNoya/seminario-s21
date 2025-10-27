-- Crear base de datos
CREATE DATABASE IF NOT EXISTS tableros;
USE tableros;

-- Tabla de interruptores diferenciales
CREATE TABLE InterruptorDiferencial (
  id INT AUTO_INCREMENT PRIMARY KEY,
  descripcion VARCHAR(255),
  amperaje INT,
  sensibilidad INT,
  precio DECIMAL(10,2)
);

-- Tabla de interruptores termomagnéticos
CREATE TABLE InterruptorTermomagnetico (
  id INT AUTO_INCREMENT PRIMARY KEY,
  descripcion VARCHAR(255),
  amperaje INT,
  precio DECIMAL(10,2)
);

-- Tabla de artefactos
CREATE TABLE Artefacto (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100),
  wattage INT,
  tipo ENUM('Iluminacion','Aparato')
);

-- Tabla de tableros principales
CREATE TABLE TableroPrincipal (
  id INT AUTO_INCREMENT PRIMARY KEY,
  descripcion VARCHAR(255),
  idInterruptorDiferencial INT UNIQUE,
  idInterruptorTermomagnetico INT UNIQUE,
  FOREIGN KEY (idInterruptorDiferencial) REFERENCES InterruptorDiferencial(id),
  FOREIGN KEY (idInterruptorTermomagnetico) REFERENCES InterruptorTermomagnetico(id)
);

-- Tabla de circuitos
CREATE TABLE Circuito (
  id INT AUTO_INCREMENT PRIMARY KEY,
  tipo ENUM('IUG','TUG','TUE'),
  calibreMaximo INT,
  cantidadBocas INT,
  idTablero INT,
  idInterruptorTermomagnetico INT UNIQUE,
  FOREIGN KEY (idTablero) REFERENCES TableroPrincipal(id),
  FOREIGN KEY (idInterruptorTermomagnetico) REFERENCES InterruptorTermomagnetico(id)
);

-- Relación uno a muchos: Artefactos por circuito
CREATE TABLE ArtefactoPorCircuito (
  idCircuito INT,
  idArtefacto INT,
  PRIMARY KEY (idCircuito, idArtefacto),
  FOREIGN KEY (idCircuito) REFERENCES Circuito(id),
  FOREIGN KEY (idArtefacto) REFERENCES Artefacto(id)
);
