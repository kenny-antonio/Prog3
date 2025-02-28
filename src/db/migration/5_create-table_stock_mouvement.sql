CREATE TABLE IF NOT EXISTS Stock_Movement (
    id SERIAL PRIMARY KEY,
    id_ingredient INT NOT NULL,
    movement_type VARCHAR(10) CHECK (movement_type IN ('ENTREE', 'SORTIE')),
    quantity DECIMAL(10, 2) NOT NULL,
    unit unit_enum NOT NULL,
    movement_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_ingredient) REFERENCES Ingredient(id_ingredient)
);
