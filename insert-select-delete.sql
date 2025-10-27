-- Inserts
INSERT INTO InterruptorDiferencial (descripcion, amperaje, sensibilidad, precio)
VALUES ('Disyuntor 25A 30mA', 25, 30, 1500.00);

INSERT INTO InterruptorTermomagnetico (descripcion, amperaje, precio)
VALUES ('IGA 2P 32A', 32, 1200.00), ('Térmico 10A', 10, 600.00);

INSERT INTO TableroPrincipal (descripcion, idInterruptorDiferencial, idInterruptorTermomagnetico)
VALUES ('Tablero para vivienda de 3 ambientes', 1, 1);

INSERT INTO Circuito (tipo, calibreMaximo, cantidadBocas, idTablero, idInterruptorTermomagnetico)
VALUES ('TUG', 2, 10, 1, 2);

INSERT INTO Artefacto (nombre, wattage, tipo)
VALUES ('Heladera', 300, 'Aparato'), ('Lámpara LED', 15, 'Iluminacion');

INSERT INTO ArtefactoPorCircuito (idCircuito, idArtefacto)
VALUES (1, 1), (1, 2);

-- Selects
SELECT a.nombre, a.wattage, a.tipo
FROM Artefacto a
JOIN ArtefactoPorCircuito apc ON a.id = apc.idArtefacto
WHERE apc.idCircuito = 1;

SELECT t.descripcion AS tablero,
       idt.descripcion AS interruptor_tablero,
       idf.descripcion AS diferencial
FROM TableroPrincipal t
JOIN InterruptorTermomagnetico idt ON t.idInterruptorTermomagnetico = idt.id
JOIN InterruptorDiferencial idf ON t.idInterruptorDiferencial = idf.id
WHERE t.id = 1;

SELECT c.tipo, c.calibreMaximo, c.cantidadBocas,
       it.descripcion AS interruptor,
       a.nombre AS artefacto, a.wattage
FROM Circuito c
JOIN InterruptorTermomagnetico it ON c.idInterruptorTermomagnetico = it.id
JOIN ArtefactoPorCircuito apc ON c.id = apc.idCircuito
JOIN Artefacto a ON apc.idArtefacto = a.id
WHERE c.id = 1;

-- Delete
DELETE FROM ArtefactoPorCircuito WHERE idArtefacto = 2;
DELETE FROM Artefacto WHERE id = 2;
DELETE FROM ArtefactoPorCircuito WHERE idCircuito = 1;
DELETE FROM Circuito WHERE id = 1;
DELETE FROM TableroPrincipal WHERE id = 1;

