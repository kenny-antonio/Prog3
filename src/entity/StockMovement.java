package entity;

import java.time.LocalDateTime;

public class StockMovement {
    public enum Type { ENTREE, SORTIE }
    public enum Unit { G, L, U }

    private int id;
    private int idIngredient;
    private Type movementType;
    private double quantity;
    private Unit unit;
    private LocalDateTime movementDatetime;

    // Constructeurs
    public StockMovement() {}

    public StockMovement(int idIngredient, Type movementType, double quantity, Unit unit, LocalDateTime movementDatetime) {
        this.idIngredient = idIngredient;
        this.movementType = movementType;
        this.quantity = quantity;
        this.unit = unit;
        this.movementDatetime = movementDatetime;
    }

    public StockMovement(int id, int idIngredient, Type movementType, double quantity, Unit unit, LocalDateTime movementDatetime) {
        this.id = id;
        this.idIngredient = idIngredient;
        this.movementType = movementType;
        this.quantity = quantity;
        this.unit = unit;
        this.movementDatetime = movementDatetime;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdIngredient() {
        return idIngredient;
    }

    public void setIdIngredient(int idIngredient) {
        this.idIngredient = idIngredient;
    }

    public Type getMovementType() {
        return movementType;
    }

    public void setMovementType(Type movementType) {
        this.movementType = movementType;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public LocalDateTime getMovementDatetime() {
        return movementDatetime;
    }

    public void setMovementDatetime(LocalDateTime movementDatetime) {
        this.movementDatetime = movementDatetime;
    }
}
