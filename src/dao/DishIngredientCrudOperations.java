package dao;
import db.DatabaseConnection;
import entity.DishIngredient;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public  class DishIngredientCrudOperations implements CrudOperation<DishIngredient> {
    public DatabaseConnection databaseConnection = new DatabaseConnection();

    @Override
    public List<DishIngredient> getAll(Integer limit, Integer offset) {
        String query = "SELECT id_dish, id_ingredient, required_quantity, unit FROM Dish_Ingredient LIMIT ? OFFSET ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, limit);
            statement.setInt(2, offset);

            try (ResultSet resultSet = statement.executeQuery()) {
                List<DishIngredient> dishIngredients = new ArrayList<>();
                while (resultSet.next()) {
                    DishIngredient dishIngredient = new DishIngredient();
                    dishIngredient.setIdDish(resultSet.getInt("id_dish"));
                    dishIngredient.setIdIngredient(resultSet.getInt("id_ingredient"));
                    dishIngredient.setRequiredQuantity(resultSet.getDouble("required_quantity"));
                    dishIngredient.setUnit(resultSet.getString("unit"));
                    dishIngredients.add(dishIngredient);
                }
                return dishIngredients;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving dish ingredients", e);
        }
    }

    @Override
    public List<DishIngredient> findById(int id_dish, String name, Double unit_price, List<DishIngredient> ingredients) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public DishIngredient findById(int id) {
        throw new UnsupportedOperationException("Not supported for many-to-many relationship.");
    }

    @Override
    public List<DishIngredient> saveAll(List<DishIngredient> dishIngredients) {
        String query = "INSERT INTO Dish_Ingredient (id_dish, id_ingredient, required_quantity, unit) " +
                "VALUES (?, ?, ?, ?) ON CONFLICT (id_dish, id_ingredient) DO UPDATE " +
                "SET required_quantity = EXCLUDED.required_quantity, unit = EXCLUDED.unit";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            for (DishIngredient dishIngredient : dishIngredients) {
                statement.setInt(1, dishIngredient.getIdDish());
                statement.setInt(2, dishIngredient.getIdIngredient());
                statement.setDouble(3, dishIngredient.getRequiredQuantity());
                statement.setString(4, dishIngredient.getUnit());
                statement.addBatch();
            }
            statement.executeBatch();
            return dishIngredients;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving dish ingredients", e);
        }
    }

    @Override
    public List<DishIngredient> findByDishId(int id_dish) {
        String query = "SELECT id_dish, id_ingredient, required_quantity, unit FROM Dish_Ingredient WHERE id_dish = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id_dish);

            try (ResultSet resultSet = statement.executeQuery()) {
                List<DishIngredient> dishIngredients = new ArrayList<>();
                while (resultSet.next()) {
                    DishIngredient dishIngredient = new DishIngredient();
                    dishIngredient.setIdDish(resultSet.getInt("id_dish"));
                    dishIngredient.setIdIngredient(resultSet.getInt("id_ingredient"));
                    dishIngredient.setRequiredQuantity(resultSet.getDouble("required_quantity"));
                    dishIngredient.setUnit(resultSet.getString("unit"));
                    dishIngredients.add(dishIngredient);
                }
                return dishIngredients;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving dish ingredients", e);
        }
    }


    public int calculateTotalCostByDishId(int id_dish, LocalDate date) {
        String query = "SELECT id_ingredient, required_quantity FROM Dish_Ingredient WHERE id_dish = ?";

        // SQL pour obtenir le prix de l'ingrédient à une date spécifique
        String priceQuery = "SELECT price FROM Ingredient_Price WHERE id_ingredient = ? AND price_date <= ? ORDER BY price_date DESC LIMIT 1";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id_dish);

            try (ResultSet resultSet = statement.executeQuery()) {
                int totalCost = 0;
                while (resultSet.next()) {
                    int ingredientId = resultSet.getInt("id_ingredient");
                    int requiredQuantity = resultSet.getInt("required_quantity");

                    // Récupérer le prix de l'ingrédient à la date spécifiée
                    try (PreparedStatement priceStatement = connection.prepareStatement(priceQuery)) {
                        priceStatement.setInt(1, ingredientId);
                        priceStatement.setDate(2, Date.valueOf(date));

                        try (ResultSet priceResultSet = priceStatement.executeQuery()) {
                            if (priceResultSet.next()) {
                                double ingredientPrice = priceResultSet.getDouble("price");
                                totalCost += ingredientPrice * requiredQuantity;
                            }
                        }
                    }
                }
                return totalCost;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error calculating total cost of ingredients", e);
        }
    }


    public int getIngredientCost(int ingredientId) {
        String query = "SELECT unit_price FROM Ingredient WHERE id_ingredient = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, ingredientId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("unit_price"); // Retourne le prix sous forme d'entier
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving ingredient cost", e);
        }

        return 0; // Si aucun ingrédient trouvé, retourne 0
    }
}
