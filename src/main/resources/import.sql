INSERT INTO customer (id, name, email, gender) VALUES (1, 'Homem Aranha', 'aranha@vingadores.com', 'M');
INSERT INTO customer (id, name, email, gender) VALUES (2, 'Thor', 'thor@vingadores.com', 'M');
INSERT INTO customer (id, name, email, gender) VALUES (3, 'Viuva Negra', 'viuva@vingadores.com', 'F');
INSERT INTO customer (id, name, email, gender) VALUES (4, 'Namor', 'namor@vingadores.com', 'M');
INSERT INTO customer (id, name, email, gender) VALUES (5, 'Gamora', 'gamora@vingadores.com', 'F');

INSERT INTO address (id, neighborhood, street, city, state, zip, type) VALUES (1, 'Centro', 'Rua 1, 1', 'São Paulo', 'SP', '00000001', 'HOME');
INSERT INTO address (id, neighborhood, street, city, state, zip, type) VALUES (2, 'Centro', 'Rua 2, 2', 'São Paulo', 'SP', '00000002', 'HOME');
INSERT INTO address (id, neighborhood, street, city, state, zip, type) VALUES (3, 'Centro', 'Rua 3, 3', 'São Paulo', 'SP', '00000003', 'HOME');
INSERT INTO address (id, neighborhood, street, city, state, zip, type) VALUES (4, 'Centro', 'Rua 4, 4', 'São Paulo', 'SP', '00000003', 'HOME');
INSERT INTO address (id, neighborhood, street, city, state, zip, type) VALUES (5, 'Centro', 'Rua 5, 5', 'São Paulo', 'SP', '00000003', 'HOME');

INSERT INTO customer_address (customer_id, address_id) VALUES (1, 1);
INSERT INTO customer_address (customer_id, address_id) VALUES (1, 2);
INSERT INTO customer_address (customer_id, address_id) VALUES (2, 2);
INSERT INTO customer_address (customer_id, address_id) VALUES (3, 3);
INSERT INTO customer_address (customer_id, address_id) VALUES (4, 4);
INSERT INTO customer_address (customer_id, address_id) VALUES (5, 5);