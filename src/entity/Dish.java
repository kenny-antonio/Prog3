package entity;
import dao.DishIngredientCrudOperations;

import java.time.LocalDate;
import java.util.List;

public class Dish {
    private int idDish;
    private String name;
    private Double unitPrice;
    private List< DishIngredient> ingredients;
    private int id;

    public  Dish () {}


    public int getIdDish(int idDish) {
        return this.idDish;
    }

    public void setIdDish(int idDish) {
        this.idDish = idDish;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getIdDish() {
        return idDish;
    }

    public List<DishIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<DishIngredient> ingredients) {
        this.ingredients = ingredients;
    }
    public int getTotalCost(LocalDate date) {
        DishIngredientCrudOperations dishIngredientCrudOperations = new DishIngredientCrudOperations();
        return dishIngredientCrudOperations.calculateTotalCostByDishId(this.id, date);
    }


}
