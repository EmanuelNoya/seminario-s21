-- Crear base de datos
CREATE DATABASE IF NOT EXISTS tableros;
USE tableros;

-- Tabla principal: Tablero
CREATE TABLE TableroPrincipal (
  id INT AUTO_INCREMENT PRIMARY KEY,
  descripcion VARCHAR(255)
);

-- Interruptores diferenciales
CREATE TABLE InterruptorDiferencial (
  id INT AUTO_INCREMENT PRIMARY KEY,
  descripcion VARCHAR(255),
  amperaje INT,
  sensibilidad INT,
  precio DECIMAL(10,2)
);

-- Interruptores termomagnéticos
CREATE TABLE InterruptorTermomagnetico (
  id INT AUTO_INCREMENT PRIMARY KEY,
  descripcion VARCHAR(255),
  amperaje INT,
  precio DECIMAL(10,2)
);

-- Artefactos eléctricos
CREATE TABLE Artefacto (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100),
  wattage INT,
  tipo ENUM('Iluminacion','Aparato')
);

-- Circuitos eléctricos
CREATE TABLE Circuito (
  id INT AUTO_INCREMENT PRIMARY KEY,
  tipo ENUM('IUG','TUG','TUE'),
  calibreMaximo INT,
  cantidadBocas INT,
  idTablero INT,
  FOREIGN KEY (idTablero) REFERENCES TableroPrincipal(id)
);

-- Relación muchos a muchos: Circuito - Artefacto
CREATE TABLE Circuito_Artefacto (
  idCircuito INT,
  idArtefacto INT,
  PRIMARY KEY (idCircuito, idArtefacto),
  FOREIGN KEY (idCircuito) REFERENCES Circuito(id),
  FOREIGN KEY (idArtefacto) REFERENCES Artefacto(id)
);

-- Relación muchos a muchos: Circuito - InterruptorTermomagnetico
CREATE TABLE Circuito_Interruptor (
  idCircuito INT,
  idInterruptor INT,
  PRIMARY KEY (idCircuito, idInterruptor),
  FOREIGN KEY (idCircuito) REFERENCES Circuito(id),
  FOREIGN KEY (idInterruptor) REFERENCES InterruptorTermomagnetico(id)
);

-- Relación muchos a muchos: Tablero - InterruptorDiferencial
CREATE TABLE Tablero_InterruptorDiferencial (
  idTablero INT,
  idInterruptor INT,
  PRIMARY KEY (idTablero, idInterruptor),
  FOREIGN KEY (idTablero) REFERENCES TableroPrincipal(id),
  FOREIGN KEY (idInterruptor) REFERENCES InterruptorDiferencial(id)
);

-- Relación muchos a muchos: Tablero - InterruptorTermomagnético
CREATE TABLE Tablero_InterruptorTermomagnetico (
  idTablero INT,
  idInterruptor INT,
  PRIMARY KEY (idTablero, idInterruptor),
  FOREIGN KEY (idTablero) REFERENCES TableroPrincipal(id),
  FOREIGN KEY (idInterruptor) REFERENCES InterruptorTermomagnetico(id)
);