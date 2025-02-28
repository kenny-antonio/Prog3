package dao;
import entity.DishIngredient;

import java.util.List;

public interface CrudOperation<E> {


    List<E> getAll(Integer limit, Integer offset);

    E findById(int id);

    List<E> saveAll(List<E> entities);

    List<E> findById(int id_dish, String name, Double unit_price, List<DishIngredient> ingredients);

    List<DishIngredient> findByDishId(int id_dish);
}
