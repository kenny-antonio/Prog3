package dao;
import db.DatabaseConnection;
import entity.Dish;
import entity.DishIngredient;
import entity.Ingredient;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public  class DishCrudOperation implements CrudOperation<Dish> {
    public final DatabaseConnection databaseConnection = new DatabaseConnection();

    @Override
    public List<Dish> getAll(Integer limit, Integer offset){
        String query = "SELECT id_dish, name, unit_price FROM Dish LIMIT ? OFFSET ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){

            statement.setInt(1, limit);
            statement.setInt(2, offset);

            try (ResultSet resultSet = statement.executeQuery()){
                List<Dish> dishes = new ArrayList<>();
                while (resultSet.next()){
                    Dish dish = new Dish();
                    dish.getIdDish(resultSet.getInt("id_dish"));
                    dish.setName(resultSet.getString("name"));
                    dish.setUnitPrice(resultSet.getDouble("unit_price"));
                    DishIngredientCrudOperations dishIngredientCrud = new DishIngredientCrudOperations();
                    List<DishIngredient> ingredients = dishIngredientCrud.findByDishId(dish.getIdDish());
                    dish.setIngredients(ingredients);
                    dishes.add(dish);
                }
                return dishes;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while retrieving dishes", e);
        }
    }

    @Override
    public Dish findById(int id) {
        String query = "SELECT id_dish, name, unit_price FROM Dish WHERE id_dish = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Dish dish = new Dish();
                    dish.setIdDish(resultSet.getInt("id_dish"));
                    dish.setName(resultSet.getString("name"));
                    dish.setUnitPrice(resultSet.getDouble("unit_price"));
                    return dish;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while retrieving the dish", e);
        }
        return null;
    }

    @Override
    public List<Dish> saveAll(List<Dish> entities) {
        String query = "INSERT INTO Dish (name, unit_price) VALUES (?, ?) ON CONFLICT (id_dish) DO UPDATE SET name = EXCLUDED.name, unit_price = EXCLUDED.unit_price RETURNING id_dish, name, unit_price";

        try (Connection connection = databaseConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

                for (Dish dish : entities) {
                    statement.setString(1, dish.getName());
                    statement.setDouble(2, dish.getUnitPrice());
                    statement.addBatch();
                }
                statement.executeBatch();

                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    List<Dish> savedDishes = new ArrayList<>();
                    while (resultSet.next()) {
                        Dish dish = new Dish();
                        dish.setIdDish(resultSet.getInt("id_dish"));
                        dish.setName(resultSet.getString("name"));
                        dish.setUnitPrice(resultSet.getDouble("unit_price"));
                        savedDishes.add(dish);
                    }
                    return savedDishes;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while saving the dishes", e);
        }
    }

    @Override
    public List<Dish> findById(int id_dish, String name, Double unit_price, List<DishIngredient> ingredients) {
        StringBuilder query = new StringBuilder("SELECT d.id_dish, d.name, d.unit_price FROM Dish d WHERE 1=1");

        List<Object> params = new ArrayList<>();

        Map<String, Object> conditions = new HashMap<>();
        conditions.put("d.id_dish", id_dish > 0 ? id_dish : null);
        conditions.put("d.name LIKE", (name != null && !name.isEmpty()) ? "%" + name + "%" : null);
        conditions.put("d.unit_price", unit_price);

        for (DishIngredient ingredient : ingredients) {
            conditions.put("i.name", ingredient.getIdIngredient());
        }

        for (Map.Entry<String, Object> entry : conditions.entrySet()) {
            if (entry.getValue() != null) {
                query.append(" AND ").append(entry.getKey()).append(" ?");
                params.add(entry.getValue());
            }
        }

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query.toString())) {

            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                List<Dish> dishes = new ArrayList<>();
                while (resultSet.next()) {
                    Dish dish = new Dish();
                    dish.setIdDish(resultSet.getInt("id_dish"));
                    dish.setName(resultSet.getString("name"));
                    dish.setUnitPrice(resultSet.getDouble("unit_price"));
                    dishes.add(dish);
                }
                return dishes;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while searching for dishes", e);
        }
    }

    @Override
    public List<DishIngredient> findByDishId(int id_dish) {
        String query = "SELECT di.id_dish_ingredient, di.id_dish, di.id_ingredient, i.name, di.quantity " +
                "FROM DishIngredient di " +
                "JOIN Ingredient i ON di.id_ingredient = i.id_ingredient " +
                "WHERE di.id_dish = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id_dish);

            try (ResultSet resultSet = statement.executeQuery()) {
                List<DishIngredient> dishIngredients = new ArrayList<>();
                while (resultSet.next()) {
                    DishIngredient dishIngredient = new DishIngredient();
                    dishIngredient.setIdIngredient(resultSet.getInt("id_dish_ingredient"));
                    dishIngredient.setIdDish(resultSet.getInt("id_dish"));
                    dishIngredient.setIdIngredient(resultSet.getInt("id_ingredient"));
                    dishIngredient.setUnit(String.valueOf(resultSet.getDouble("quantity")));

                    Ingredient ingredient = new Ingredient();
                    ingredient.setIdIngredient(resultSet.getInt("id_ingredient"));
                    ingredient.setName(resultSet.getString("name"));
                    dishIngredient.setIdIngredient(ingredient.getIdIngredient());

                    dishIngredients.add(dishIngredient);
                }
                return dishIngredients;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while retrieving dish ingredients", e);
        }
    }

    public List<Ingredient> filterAndSortIngredients(int limit, int offset) {
        String query = "SELECT * FROM Ingredient " +
                "WHERE name ILIKE ? AND unit_price <= ? " +
                "ORDER BY name ASC, unit_price DESC " +
                "LIMIT ? OFFSET ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, "%u%");
            statement.setInt(2, 1000);
            statement.setInt(3, limit);
            statement.setInt(4, offset);

            try (ResultSet resultSet = statement.executeQuery()) {
                List<Ingredient> ingredients = new ArrayList<>();
                while (resultSet.next()) {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setIdIngredient(resultSet.getInt("id_ingredient"));
                    ingredient.setName(resultSet.getString("name"));
                    ingredient.setUnitPrice(resultSet.getInt("unit_price"));
                    ingredients.add(ingredient);
                }
                return ingredients;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while filtering ingredients", e);
        }
    }

}
