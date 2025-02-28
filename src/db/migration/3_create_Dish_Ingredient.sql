-- Créer la table Dish_Ingredient (jointure entre les plats et les ingrédients)
CREATE TABLE Dish_Ingredient (
    id_dish INTEGER REFERENCES Dish(id_dish) ON DELETE CASCADE,  -- Clé étrangère vers Dish
    id_ingredient INTEGER REFERENCES Ingredient(id_ingredient) ON DELETE CASCADE,  -- Clé étrangère vers Ingredient
    required_quantity DECIMAL(10, 2) NOT NULL,  -- Quantité requise de l'ingrédient
    unit unit_enum NOT NULL,  -- Unité pour la quantité de l'ingrédient
    PRIMARY KEY (id_dish, id_ingredient)  -- Clé primaire composite
);