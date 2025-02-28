package dao;

import db.DatabaseConnection;
import entity.StockMovement;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StockMovementCrudOperation {
    private final DatabaseConnection databaseConnection = new DatabaseConnection();

    /**
     * Méthode pour ajouter un mouvement de stock (entrée ou sortie)
     */
    public void addStockMovement(StockMovement movement) {
        String query = "INSERT INTO Stock_Movement (id_ingredient, movement_type, quantity, unit, movement_datetime) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, movement.getIdIngredient());
            statement.setString(2, movement.getMovementType().name());
            statement.setDouble(3, movement.getQuantity());
            statement.setString(4, movement.getUnit().name());
            statement.setTimestamp(5, Timestamp.valueOf(movement.getMovementDatetime()));

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while adding stock movement", e);
        }
    }

    /**
     * Méthode pour obtenir l'état des stocks d'un ingrédient à une date donnée
     */
    public double getStockAtDate(int idIngredient, LocalDateTime date) {
        String query = "SELECT SUM(CASE WHEN movement_type = 'ENTREE' THEN quantity ELSE -quantity END) AS stock_disponible " +
                "FROM Stock_Movement " +
                "WHERE id_ingredient = ? AND movement_datetime <= ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, idIngredient);
            statement.setTimestamp(2, Timestamp.valueOf(date));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("stock_disponible");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while retrieving stock", e);
        }
        return 0.0; // Si aucun stock trouvé, retourne 0
    }

    /**
     * Méthode pour récupérer tous les mouvements de stock d'un ingrédient
     */
    public List<StockMovement> getStockMovements(int idIngredient) {
        String query = "SELECT id, id_ingredient, movement_type, quantity, unit, movement_datetime FROM Stock_Movement WHERE id_ingredient = ?";
        List<StockMovement> movements = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, idIngredient);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    StockMovement movement = new StockMovement(
                            resultSet.getInt("id"),
                            resultSet.getInt("id_ingredient"),
                            StockMovement.Type.valueOf(resultSet.getString("movement_type")),
                            resultSet.getDouble("quantity"),
                            StockMovement.Unit.valueOf(resultSet.getString("unit")),
                            resultSet.getTimestamp("movement_datetime").toLocalDateTime()
                    );
                    movements.add(movement);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while retrieving stock movements", e);
        }
        return movements;
    }
}
