package com.example.saleshandlingapp.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Klasa reprezentująca element zlecenia(pozycje) w systemie obsługi sprzedaży.
 */
@Entity(tableName = "OrderItems")
public class OrderItem {

    /**
     * Unikalny identyfikator elementu zlecenia generowany automatycznie przez bazę danych.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    private int orderItemID;

    /**
     * Numer identyfikacyjny zlecenia do którego przypisana jest pozycja
     */
    @ColumnInfo(name = "OrderID")
    private int orderID;

    /**
     * Numer identyfikacyjny produktu który jest częscią.
     */
    @ColumnInfo(name = "ProductID")
    private int productID;

    /**
     * Ilość produktu w danej pozycji zlecenia.
     */
    @ColumnInfo(name = "Amount")
    private int amount;

    /**
     * Cena jednostkowa produktu w danej pozycji.
     */
    @ColumnInfo(name = "ItemPrice")
    private double itemPrice;

    /**
     * Pobiera unikalny identyfikator pozycji zlecenia.
     * @return identyfikator pozycji zlecenia.
     */
    public int getOrderItemID() {
        return orderItemID;
    }

    /**
     * Ustawia identyfikator pozycji zlecenia.
     * @param orderItemID identyfikator pozycji zlecenia.
     */
    public void setOrderItemID(int orderItemID) {
        this.orderItemID = orderItemID;
    }

    /**
     * Pobiera numer identyfikacyjny zlecenia do którego przypisana jest pozycja.
     * @return numer identyfikacyjny zlecenia.
     */
    public int getOrderID() {
        return orderID;
    }

    /**
     * Ustawia numer identyfikacyjny zlecenia do którego przypisana jest pozycja
     * @param orderID numer identyfikacyjny zlecenia.
     */
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    /**
     * Pobiera numer identyfikacyjny produktu do którego przypisana jest pozycja.
     * @return numer identyfikacyjny produktu.
     */
    public int getProductID() {
        return productID;
    }

    /**
     * Ustawia numer identyfikacyjny produktu do którego przypisana jest pozycja
     * @param productID numer identyfikacyjny produktu.
     */
    public void setProductID(int productID) {
        this.productID = productID;
    }

    /**
     * Pobiera ilość produktów w danej pozycji zlecenia.
     * @return ilość produktów.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Ustawia ilość produktów w danej pozycji zlecenia.
     * @param amount ilość produktów.
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Pobiera cenę jednostkową produktu w danej pozycji zlecenia.
     * @return cena jednostkowa produktu.
     */
    public double getItemPrice() {
        return itemPrice;
    }

    /**
     * Ustawia cenę jednostkową produktu w danej pozycji zlecenia.
     * @return cena jednostkowa produktu.
     */
    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }
}
