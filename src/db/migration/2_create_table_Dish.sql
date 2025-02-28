-- Créer la table Dish (plat)
CREATE TABLE Dish (
    id_dish SERIAL PRIMARY KEY,  -- Clé primaire
    name VARCHAR(255) NOT NULL,  -- Nom du plat
    unit_price DECIMAL(10, 2) NOT NULL  -- Prix unitaire du plat
);
