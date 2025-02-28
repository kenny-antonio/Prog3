CREATE USER admin WITH PASSWORD '';

GRANT ALL PRIVILEGES ON DATABASE restauration TO admin;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO ton_utilisateur;


-- Créer le type ENUM pour les unités
CREATE TYPE unit_enum AS ENUM ('G', 'L', 'U');

-- Créer la table Dish (plat)
CREATE TABLE Dish (
    id_dish SERIAL PRIMARY KEY,  -- Clé primaire
    name VARCHAR(255) NOT NULL,  -- Nom du plat
    unit_price DECIMAL(10, 2) NOT NULL  -- Prix unitaire du plat
);

-- Créer la table Ingredient (ingrédient)
CREATE TABLE Ingredient (
    id_ingredient SERIAL PRIMARY KEY,  -- Clé primaire
    name VARCHAR(255) NOT NULL,  -- Nom de l'ingrédient
    unit_price DECIMAL(10, 2) NOT NULL,  -- Prix unitaire de l'ingrédient
    unit unit_enum NOT NULL,  -- Unité de mesure de l'ingrédient
    update_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- Date de mise à jour
);

-- Créer la table Dish_Ingredient (jointure entre les plats et les ingrédients)
CREATE TABLE Dish_Ingredient (
    id_dish INTEGER REFERENCES Dish(id_dish) ON DELETE CASCADE,  -- Clé étrangère vers Dish
    id_ingredient INTEGER REFERENCES Ingredient(id_ingredient) ON DELETE CASCADE,  -- Clé étrangère vers Ingredient
    required_quantity DECIMAL(10, 2) NOT NULL,  -- Quantité requise de l'ingrédient
    unit unit_enum NOT NULL,  -- Unité pour la quantité de l'ingrédient
    PRIMARY KEY (id_dish, id_ingredient)  -- Clé primaire composite
);

-- Insérer des ingrédients
INSERT INTO Ingredient (name, unit_price, unit) VALUES
('Saucisse', 20.00, 'G'),
('Huile', 10000.00, 'L'),
('Oeuf', 1000.00, 'U'),
('Pain', 1000.00, 'U');

-- Insérer des plats (Dish)
INSERT INTO Dish (name, unit_price) VALUES
('Hot Dog', 15000.00);

-- Lier les ingrédients au plat "Hot Dog" avec les quantités nécessaires
INSERT INTO Dish_Ingredient (id_dish, id_ingredient, required_quantity, unit) VALUES
(1, 1, 100, 'G'),  -- 100g de Saucisse
(1, 2, 0.15, 'L'),  -- 0.15 L d'Huile
(1, 3, 1, 'U'),    -- 1 Oeuf
(1, 4, 1, 'U');    -- 1 Pain


