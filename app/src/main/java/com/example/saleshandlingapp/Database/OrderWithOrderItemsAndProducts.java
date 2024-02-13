package com.example.saleshandlingapp.Database;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

/**
 * Klasa reprezentująca obiekt zawierający informacje o zleceniu (Order) razem ze składającym je klientem (Client)
 * oraz listą pozycji zamówienia (OrderItemAndProduct).
 */
public class OrderWithOrderItemsAndProducts {
    /**
     * Obiekt reprezentujący zlecenie.
     */
    @Embedded
    public Order order;

    /**
     * Obiekt reprezentujący klienta składającego zlecenie.
     */
    @Relation(
            parentColumn = "ClientID",
            entityColumn = "ID"
    )
    public Client client;

    /**
     * Lista obiektów OrderItemAndProduct reprezentujących pozycje zamówienia razem z produktami.
     */
    @Relation(
            entity = OrderItem.class,
            parentColumn = "ID",
            entityColumn = "OrderID"
    )
    public List<OrderItemAndProduct> orderItems;
}
