CREATE TABLE Ingredient_Price (
    id SERIAL PRIMARY KEY,                   -- Identifiant unique
    id_ingredient INT REFERENCES Ingredient(id_ingredient), -- Référence à l'ingrédient
    price DECIMAL(10, 2) NOT NULL,           -- Prix de l'ingrédient
    price_date DATE DEFAULT CURRENT_DATE,    -- Date du prix
    CONSTRAINT fk_ingredient FOREIGN KEY (id_ingredient) REFERENCES Ingredient(id_ingredient)
);
