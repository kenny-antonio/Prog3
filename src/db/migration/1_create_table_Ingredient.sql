-- Créer la table Ingredient (ingrédient)
CREATE TABLE Ingredient (
    id_ingredient SERIAL PRIMARY KEY,  -- Clé primaire
    name VARCHAR(255) NOT NULL,  -- Nom de l'ingrédient
    unit_price DECIMAL(10, 2) NOT NULL,  -- Prix unitaire de l'ingrédient
    unit unit_enum NOT NULL,  -- Unité de mesure de l'ingrédient
    update_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- Date de mise à jour
);