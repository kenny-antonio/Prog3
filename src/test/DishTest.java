package test;

import com.sun.jdi.connect.spi.Connection;
import dao.DishCrudOperation;
import entity.Dish;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DishTest {
    private DishCrudOperation dishCrud;

    @BeforeEach
    void setUp() {
        dishCrud = new DishCrudOperation();
    }

    @Test
    void testGetAll() {
        // Données de test
        int limit = 5;
        int offset = 0;

        // Méthode à tester
        List<Dish> dishes = dishCrud.getAll(limit, offset);

        // Vérifications
        assertNotNull(dishes);
        assertTrue(dishes.size() <= limit);
    }

    @Test
    void testFindById() {
        // Données de test
        int dishId = 1;

        // Méthode à tester
        Dish dish = dishCrud.findById(dishId);

        // Vérifications
        assertNotNull(dish);
        assertEquals(dishId, dish.getIdDish());
    }

     /*   @Test
        public void testCalculateTotalCostByDate() {
            LocalDate currentDate = LocalDate.now();
            LocalDate pastDate = currentDate.minusDays(1);

            // Ajouter des prix à la date actuelle
            addPriceForIngredient(1, 150, currentDate); // prix de l'ingrédient 1 pour aujourd'hui
            addPriceForIngredient(2, 200, currentDate); // prix de l'ingrédient 2 pour aujourd'hui

            // Ajouter des prix à une date antérieure
            addPriceForIngredient(1, 100, pastDate); // prix de l'ingrédient 1 pour hier
            addPriceForIngredient(2, 180, pastDate); // prix de l'ingrédient 2 pour hier

            // Calculer le coût pour la date actuelle
            int currentCost = dishCrud.getTotalCost(currentDate);
            assertEquals(350, currentCost); // Le coût total des ingrédients pour aujourd'hui

            // Calculer le coût pour la date antérieure
            int pastCost = dishCrud.getTotalCost(pastDate);
            assertEquals(280, pastCost); // Le coût total des ingrédients pour hier
        }

        private void addPriceForIngredient(int ingredientId, double price, LocalDate date) {
            String query = "INSERT INTO Ingredient_Price (id_ingredient, price, price_date) VALUES (?, ?, ?)";
            try (Connection connection = databaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, ingredientId);
                statement.setDouble(2, price);
                statement.setDate(3, Date.valueOf(date));
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Error inserting price for ingredient", e);
            }

        }*/
}

