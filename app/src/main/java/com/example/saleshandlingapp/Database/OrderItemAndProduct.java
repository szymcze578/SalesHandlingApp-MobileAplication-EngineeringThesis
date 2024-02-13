package com.example.saleshandlingapp.Database;

import androidx.room.Embedded;
import androidx.room.Relation;


/**
 * Klasa reprezentująca powiązanie między pozycją zlecenia (OrderItem) a produktem (Product) w systemie.
 * Wykorzystywana do pobierania informacji o produktach w ramach danej pozycji zlecnia.
 */
public class OrderItemAndProduct {
    /**
     * Obiekt reprezentujący pozycję zlecenia.
     */
    @Embedded public OrderItem orderItem;

    /**
     * Obiekt reprezentujący produkt powiązany z daną pozycją zlecenia.
     */
    @Relation(
            parentColumn = "ProductID",
            entityColumn = "ID"
    )
    public Product product;
}
