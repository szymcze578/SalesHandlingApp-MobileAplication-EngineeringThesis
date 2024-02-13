package com.example.saleshandlingapp.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.saleshandlingapp.Enum.OrderStatus;
import com.example.saleshandlingapp.Enum.OrderType;

import java.time.LocalDateTime;

/**
 * Klasa reprezentująca pojedyncze zlecenia.
 */
@Entity(tableName = "Orders")
public class Order {

    /**
     * Unikalny identyfikator zlecenia generowany automatycznie.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    private int orderID;

    /**
     * Identyfikator klienta składającego zlecenie.
     */
    @ColumnInfo(name = "ClientID")
    private int clientID;

    /**
     * Data utworzenia zlecenia.
     */
    @ColumnInfo(name = "CreationDate")
    private LocalDateTime creationDate;

    /**
     * Data realizacji zlecenia.
     */
    @ColumnInfo(name = "RealizationDate")
    private LocalDateTime realizationDate;

    /**
     * Status zlecenia (niezreazliowane, zrealizowane, anulowane).
     */
    @ColumnInfo(name = "OrderStatus")
    private OrderStatus orderStatus;

    /**
     * Całkowita wartość zlecenia.
     */
    @ColumnInfo(name = "Price")
    private double totalPrice;
    /**
     * Typ zlecenia (Zamówienie, Dostawa).
     */
    @ColumnInfo(name = "Type")
    private OrderType type;

    /**
     * Konstruktor klasy Order.
     * @param clientID numer identyfikacyjny klienta składającego zlecenie.
     * @param creationDate data utworzenia zlecenia.
     * @param orderStatus status zlecenia.
     * @param totalPrice całkowita wartość.
     * @param type typ zlecenia.
     */
    public Order(int clientID, LocalDateTime creationDate, OrderStatus orderStatus, double totalPrice, OrderType type) {
        this.clientID = clientID;
        this.creationDate = creationDate;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.type = type;
    }

    /**
     * Pobiera numer identyfikacyjny zlecenia.
     * @return numer identyfikacyjny zlecenia.
     */
    public int getOrderID() {
        return orderID;
    }

    /**
     * Ustawia numer identyfikacyjny zlecenia.
     * @param orderID number identyfikacyjny zlecenia.
     */
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    /**
     * Pobiera identyfikator klienta składającego zlecenia.
     * @return identyfikator klienta składającego zlecenia.
     */
    public int getClientID() {
        return clientID;
    }

    /**
     * Ustawia identyfikator klienta skłądającego zlecenie.
     * @param clientID identyfikator klienta skłądającego zlecenie.
     */
    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    /**
     * Pobiera datę utworzenia zlecenia.
     * @return data utworzenia zlecenia.
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Ustawia datę utworzenia zlecenia.
     * @param creationDate data utworzenia zlecenia.
     */
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Pobiera datę realizacji zlecenia.
     * @return data realizacji zlecenia.
     */
    public LocalDateTime getRealizationDate() {
        return realizationDate;
    }

    /**
     * Ustawia datę realizacji zlecenia.
     * @param realizationDate data realizacji zlecenia.
     */
    public void setRealizationDate(LocalDateTime realizationDate) {
        this.realizationDate = realizationDate;
    }

    /**
     * Pobiera status zlecenia w formie wyliczeniowego typu danych OrderStatus.
     * @return status zlecenia.
     */
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    /**
     * Ustawia status zlecenia.
     * @param orderStatus status zlecenia do ustawienia.
     */
    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * Pobiera całkowitą wartość zlecenia.
     * @return całkowita wartość zlecenia.
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Ustawia całkowitą wartość zlecenia.
     * @param totalPrice całkowita wartość zlecenia do ustawienia.
     */
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * Pobiera typ zlecenia (Zamówienie dostawa).
     * @return typ zlecenia.
     */
    public OrderType getType() {
        return type;
    }

    /**
     * Ustawia typ zlecenia.
     * @param type typ zlecenia.
     */
    public void setType(OrderType type) {
        this.type = type;
    }
}
