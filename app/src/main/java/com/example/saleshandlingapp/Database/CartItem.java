package com.example.saleshandlingapp.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Klasa reprezentująca element koszyka w systemie obsługi sprzedaży.
 * Obiekty tej klasy są przechowywane w bazie danych.
 */
@Entity(tableName = "CartItems")
public class CartItem {

    /**
     * Unikalny identyfikator elementu koszyka generowany automatycznie przez bazę danych.
     */
    @ColumnInfo(name = "ID")
    @PrimaryKey(autoGenerate = true)
    private int cartItemID;

    /**
     * Identyfikator produktu, z którym powiązany jest ten element koszyka.
     */
    @ColumnInfo(name = "ProductID")
    private int productID;

    /**
     * Ilość danego produktu w koszyku.
     */
    @ColumnInfo(name = "Amount")
    private int amount;

    /**
     * Pobiera unikalny identyfikator elementu koszyka.
     *
     * @return Unikalny identyfikator elementu koszyka.
     */
    public int getCartItemID() {
        return cartItemID;
    }

    /**
     * Ustawia unikalny identyfikator elementu koszyka.
     *
     * @param cartItemID Unikalny identyfikator elementu koszyka.
     */
    public void setCartItemID(int cartItemID) {
        this.cartItemID = cartItemID;
    }

    /**
     * Pobiera identyfikator produktu powiązanego z elementem koszyka.
     *
     * @return Identyfikator produktu.
     */
    public int getProductID() {
        return productID;
    }

    /**
     * Ustawia identyfikator produktu powiązanego z elementem koszyka.
     *
     * @param productID Identyfikator produktu.
     */
    public void setProductID(int productID) {
        this.productID = productID;
    }

    /**
     * Pobiera ilość danego produktu w koszyku.
     *
     * @return Ilość produktu w koszyku.
     */
    public int getAmount() {
        return amount;
    }


    /**
     * Ustawia ilość danego produktu w koszyku.
     *
     * @param amount Ilość produktu w koszyku.
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
