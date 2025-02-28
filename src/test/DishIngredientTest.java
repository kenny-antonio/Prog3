package test;

import dao.DishIngredientCrudOperations;
import entity.DishIngredient;
import db.DatabaseConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DishIngredientTest {

    private DishIngredientCrudOperations dishIngredientCrudOperations;

    @BeforeEach
    public void setUp() throws SQLException {
        // Initialisation de la connexion à la base de données en mémoire
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();

        // Création des tables nécessaires pour les tests
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS Ingredient  (id_ingredient SERIAL PRIMARY KEY, name VARCHAR(255), unit_price DECIMAL(10, 2), unit VARCHAR(10))");
            stmt.execute("CREATE TABLE IF NOT EXISTS Dish_Ingredient (id_dish SERIAL, id_ingredient INTEGER, required_quantity DECIMAL(10, 2), unit VARCHAR(10), PRIMARY KEY(id_dish, id_ingredient))");

            // Insérer des données pour les tests
            stmt.execute("INSERT INTO Ingredient (name, unit_price, unit) VALUES ('Tomato', 1.50, 'KG')");
            stmt.execute("INSERT INTO Ingredient (name, unit_price, unit) VALUES ('Cheese', 2.00, 'KG')");
            stmt.execute("INSERT INTO Dish_Ingredient (id_dish, id_ingredient, required_quantity, unit) VALUES (1, 1, 0.5, 'KG')");
            stmt.execute("INSERT INTO Dish_Ingredient (id_dish, id_ingredient, required_quantity, unit) VALUES (1, 2, 0.3, 'KG')");
        }

        // Instanciation de l'objet à tester
        dishIngredientCrudOperations = new DishIngredientCrudOperations();
        dishIngredientCrudOperations.databaseConnection = databaseConnection;
    }

    @Test
    public void testGetAll() throws SQLException {
        // Test de la méthode getAll
        List<DishIngredient> result = dishIngredientCrudOperations.getAll(10, 0);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getIdDish());
        assertEquals(1, result.get(0).getIdIngredient());
        assertEquals(0.5, result.get(0).getRequiredQuantity());
        assertEquals("KG", result.get(0).getUnit());
    }

  /*  @Test
    public void testCalculateTotalCostByDishId() throws SQLException {
        // Test du calcul du coût total par ID de plat
        int totalCost = dishIngredientCrudOperations.calculateTotalCostByDishId(1, date);
        assertEquals(3.0, totalCost);  // 0.5 * 1.5 + 0.3 * 2 = 3.0
    }
*/
    @Test
    public void testGetIngredientCost() throws SQLException {
        // Test du calcul du coût d'un ingrédient
        int cost = dishIngredientCrudOperations.getIngredientCost(1);
        assertEquals(1.5, cost);
    }

    @Test
    public void testSaveAll() throws SQLException {
        // Test de la méthode saveAll
        DishIngredient dishIngredient = new DishIngredient();
        dishIngredient.setIdDish(1);
        dishIngredient.setIdIngredient(1);
        dishIngredient.setRequiredQuantity(1.0);
        dishIngredient.setUnit("KG");

        List<DishIngredient> ingredients = List.of(dishIngredient);

        List<DishIngredient> savedIngredients = dishIngredientCrudOperations.saveAll(ingredients);

        assertNotNull(savedIngredients);
        assertEquals(1, savedIngredients.size());
        assertEquals(1, savedIngredients.get(0).getIdDish());
        assertEquals(1, savedIngredients.get(0).getIdIngredient());
        assertEquals(1.0, savedIngredients.get(0).getRequiredQuantity());
        assertEquals("KG", savedIngredients.get(0).getUnit());
    }
}