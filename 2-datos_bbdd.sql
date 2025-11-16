-- Popular la base
INSERT INTO InterruptorDiferencial (descripcion, amperaje, sensibilidad, precio) VALUES 
('Disyuntor 25A 30mA', 25, 30, 1500.00);

INSERT INTO InterruptorTermomagnetico (descripcion, amperaje, precio) VALUES 
('IGA 2P 32A', 32, 1200.00),
('IGA 2P 16A', 16, 800.00),
('Térmico 10A', 10, 600.00),
('Térmico 16A', 16, 700.00);

INSERT INTO Artefacto (nombre, wattage, tipo) VALUES 
('Heladera', 150, 'Aparato'),
('Heladera con freezer', 200, 'Aparato'),
('TV LED', 180, 'Aparato'),
('Lámpara LED', 9, 'Iluminacion'),
('Lámpara LED', 11, 'Iluminacion'),
('Lámpara Incandecente', 75, 'Iluminacion');
