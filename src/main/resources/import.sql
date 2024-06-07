insert into chef (id, name, surname, date_of_birth) values(nextval('chef_id_seq'), 'Bruno', 'Barbieri','1962-01-12');
--insert into chef (id, name, surname, date_of_birth) values(nextval('chef_seq'), 'Alessandro', 'Borghese','1976-11-19');

INSERT INTO ingredient (name) VALUES ('Pasta');
INSERT INTO ingredient (name) VALUES ('Pomodoro');
INSERT INTO ingredient (name) VALUES ('Parmigiano');

INSERT INTO unit (name) VALUES ('grammi');
INSERT INTO unit (name) VALUES ('millilitri');

INSERT INTO quantity (amount) VALUES (200);
INSERT INTO quantity (amount) VALUES (300);

INSERT INTO recipe (name, chef_id) VALUES ('Pasta al Pomodoro',1);

INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity_id, unit_id) VALUES (1, 1, 1, 1);
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity_id, unit_id) VALUES (1, 2, 2, 1);
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity_id, unit_id) VALUES (1, 3, 1, 1);

-- Popolamento della tabella Unit
--INSERT INTO unit (id, unit) VALUES (nextval('unit_seq'), 'grammi');
--INSERT INTO unit (id, unit) VALUES (nextval('unit_seq'), 'millilitri');

-- Popolamento della tabella Quantity
--INSERT INTO quantity (id, amount, unit_id) VALUES (nextval('quantity_seq'), 200, 1); -- 100 grammi
--INSERT INTO quantity (id, amount, unit_id) VALUES (nextval('quantity_seq'), 150, 2); -- 150 millilitri

-- Popolamento della tabella Recipe
--INSERT INTO recipe (id, name) VALUES (nextval('recipe_seq'), 'Pasta al Pomodoro');

-- Popolamento della tabella Ingredient
--INSERT INTO ingredient (id, name, quantity_id, recipe_id) VALUES (nextval('ingredient_seq'), 'Pasta', 1, 1); -- 200 grammi di Pasta
--INSERT INTO ingredient (id, name, quantity_id, recipe_id) VALUES (nextval('ingredient_seq'), 'Pomodoro', 2, 1); -- 150 millilitri di Pomodoro
--INSERT INTO ingredient (id, name, quantity_id, recipe_id) VALUES (nextval('ingredient_seq'), 'Parmigiano', 1, 1); -- 200 grammi di Parmigiano
