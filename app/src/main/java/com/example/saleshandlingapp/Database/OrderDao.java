package com.example.saleshandlingapp.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.saleshandlingapp.Enum.OrderStatus;
import com.example.saleshandlingapp.Enum.OrderType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interfejs Data Access Object (DAO) definiujący operacje wykonywane na tabeli Orders oraz z nią powiązanych.
 */
@Dao
public interface OrderDao {

    /**
     * Metoda pobierająca wszystkie zlecenia.
     * @return lista wszystkich zleceń z bazy danych.
     */
    @Query("SELECT * FROM Orders")
    List<Order> getAllOrders();

    /**
     * Dodaje nowe zlecenia do bazy danych.
     * @param order zlecenie do dodania.
     */
    @Insert
    long insertOrders(Order order);

    /**
     * Pobiera listę zleceń niezrealizowanych wraz z pozycjami zlecenia i produktami.
     * @return Lista zleceń z pozycjami i produktami.
     */
    @Transaction
    @Query("SELECT * FROM Orders WHERE OrderStatus == 0")
    List<OrderWithOrderItemsAndProducts> getOrdersWithOrderAndProducts();

    /**
     * Pobiera listę zleceń zakończonych (zrealizowanych i anulowanych) wraz z pozycjami zlecenia i produktami.
     * @return Lista zleceń zakończonych wraz z pozycjami i produktami.
     */
    @Transaction
    @Query("SELECT * FROM Orders WHERE OrderStatus != 0")
    List<OrderWithOrderItemsAndProducts> getOrdersHistory();

    /**
     * Aktualizuje status zlecenia o podanym identyfikatorze.
     * @param orderID     identyfikator zlecenia.
     * @param orderStatus nowy status zlecenia.
     */
    @Query("Update Orders SET OrderStatus = :orderStatus WHERE ID = :orderID")
    void updateOrderStatus(int orderID, OrderStatus orderStatus);

    /**
     * Aktualizuje datę realizacji zlecenia o podanym identyfikatorze.
     * @param orderID           identyfikator zlecenia.
     * @param realizationDate   nowa data realizacji zlecenia.
     */
    @Query("Update Orders SET RealizationDate = :realizationDate WHERE ID = :orderID")
    void updateRealizationDate(int orderID, LocalDateTime realizationDate);

    /**
     * Pobiera listę zleceń zrealizowanych wraz z pozycjami i produktami w określonym zakresie dat.
     * @param startDate data początkowa zakresu.
     * @param endDate   data końcowa zakresu.
     * @param status    status zlecenia.
     * @param type      typ zlecenia.
     * @return lista zleceń z jego pozycjami i produktami.
     */
    @Transaction
    @Query("SELECT * FROM Orders WHERE RealizationDate BETWEEN :startDate AND :endDate AND OrderStatus = :status AND Type = :type")
    List<OrderWithOrderItemsAndProducts> getOrdersBetween(LocalDateTime startDate, LocalDateTime endDate, OrderStatus status, OrderType type);
}
