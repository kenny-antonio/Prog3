package test;


import dao.StockMovementCrudOperation;
import entity.StockMovement;
import org.junit.jupiter.api.*;
        import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StockMovementTest {
    private static final StockMovementCrudOperation stockMovementCrudOperation = new StockMovementCrudOperation();

    @Test
    @Order(1)
    void testAddStockMovement() {
        StockMovement movement = new StockMovement(1, StockMovement.Type.ENTREE, 100, StockMovement.Unit.U, LocalDateTime.now());
        stockMovementCrudOperation.addStockMovement(movement);
        assertTrue(stockMovementCrudOperation.getStockMovements(1).size() > 0);
    }

    @Test
    @Order(2)
    void testGetStockAtDate() {
        LocalDateTime date = LocalDateTime.now();
        double stock = stockMovementCrudOperation.getStockAtDate(1, date);
        assertEquals(100, stock, 0.01);
    }
}
