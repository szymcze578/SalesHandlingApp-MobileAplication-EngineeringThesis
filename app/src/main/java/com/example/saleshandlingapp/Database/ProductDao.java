package com.example.saleshandlingapp.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Interfejs Data Access Object (DAO) definiujący operacje wykonywane na tabeli Products.
 */
@Dao
public interface ProductDao {

    /**
     * Metoda pobierająca wszystkie produkty.
     * @return lista wszystkich produktów z bazy danych.
     */
    @Query("Select * FROM Products")
    List<Product> getAll();

    /**
     * Pobiera produkty z bazy danych na podstawie numeru identyfikacyjnego.
     * @param id numer identyfikacyjny produktu.
     * @return produkt o podanym numerze identyfikacyjnym.
     */
    @Query("Select * FROM Products WHERE ID = :id")
    Product getProductByID(int id);

    /**
     * Dodaje nowe produkty do bazy danych.
     * @param products produkt do dodania.
     */
    @Insert
    void insertAll(Product ... products);

    /**
     * Metoda zwiększająca ilość produktu o danym numerze identyfikacyjnym o podaną wartość.
     * @param id numer identyfikacyjny produktu.
     * @param value ilość do dodania.
     */
    @Query("Update Products SET Amount = Amount + :value WHERE ID = :id")
    void addToWarehouse(int id, int value);

    /**
     * Metoda zmniejszająca ilość produktu o danym numerze identyfikacyjnym o podaną wartość.
     * @param id numer identyfikacyjny produktu.
     * @param value ilość do odjęcia.
     */
    @Query("Update Products SET Amount = Amount - :value WHERE ID = :id")
    void takeFromWarehouse(int id, int value);

    /**
     * Metoda aktualizująca produkt o danym numerze identyfikacyjnym.
     * @param amount ilość produktu.
     * @param price cena produktu.
     * @param id numer identyfikacyjny produktu.
     */
    @Query("Update Products SET Amount = :amount, `Price(unit/kg)` = :price WHERE ID = :id")
    void updateProduct(int amount,double price, int id);
}
