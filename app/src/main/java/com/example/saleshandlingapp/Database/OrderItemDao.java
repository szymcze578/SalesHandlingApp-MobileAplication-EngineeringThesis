package com.example.saleshandlingapp.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Interfejs Data Access Object (DAO) definiujący operacje wykonywane na tabeli OrderItems.
 */
@Dao
public interface OrderItemDao {

    /**
     * Metoda pobierająca wszystkie pozycje zleceń.
     * @return lista wszystkich pozycji zleceń.
     */
    @Query("SELECT * FROM OrderItems")
    List<OrderItem> getAllOrderItems();

    /**
     * Metoda dodająca nowe pozycje zlecenia do tabeli
     * @param orderItem pozycja zlecenia.
     */
    @Insert
    void insertOrderItem(OrderItem ... orderItem);
}
