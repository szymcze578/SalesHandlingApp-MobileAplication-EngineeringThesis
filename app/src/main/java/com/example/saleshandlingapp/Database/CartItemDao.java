package com.example.saleshandlingapp.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Interfejs Data Access Object (DAO) definiujący operacje wykonywane na tabeli CartItems oraz z nią powiązanych.
 */
@Dao
public interface CartItemDao {

    /**
     * Metoda pobierająca wszystkie elementy koszyka.
     * @return lista wszytskich elementów koszyka.
     */
    @Query("SELECT * FROM CartItems")
    List<CartItem> getAllCartItems();

    /**
     * Dodaje nowe elementy koszyka do bazy danych.
     * @param cartItem element koszyka do dodania.
     */
    @Insert
    void insertCartItem(CartItem ... cartItem);

    /**
     * Usuwa produkt z koszyka na podstawie jego identyfikatora.
     * @param id identyfikator produktu do usunięcia.
     */
    @Query("DELETE FROM CartItems WHERE ProductID=:id")
    void deleteProductFromCart(int id);

    /**
     * Pobiera listę elementów koszyka wraz z informacjami o powiązanych produktach.
     * @return lista elementów koszyka wraz z powiązanymi produktami.
     */
    @Query("Select * FROM CartItems")
    List<CartItemAndProduct> getCartList();

    /**
     * Zwiększa ilość produktu o danym identyfikatorze o określoną wartość.
     * @param id identyfikator produkt.
     * @param value wartość do dodania do produktu w koszyku.
     */
    @Query("Update CartItems SET Amount = Amount + :value WHERE ProductID = :id")
    void increaseProductAmount(int id, int value);

    /**
     *
     */
    @Query("DELETE FROM CartItems")
    void deleteAll();
}
