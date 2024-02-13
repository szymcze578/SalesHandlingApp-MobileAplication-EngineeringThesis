package com.example.saleshandlingapp.Database;

import androidx.room.Embedded;
import androidx.room.Relation;

/**
 * Klasa reprezentująca połączanie między tablelami bazy danych.
 * W tym przypadku elementami koszyka(CartItem) a produktami(Product).
 * Używana w kontekście zapytań Room, pobierających dane z dwóch encji jednocześnie.
 */
public class CartItemAndProduct {

    /**
     * Obiekt klasy CartItem reprezentujący element koszyka.
     */
    @Embedded
    public CartItem cartItem;

    /**
     * Obiekt klasy Product reprezentujący powiązany produkt.
     */
    @Relation(
            parentColumn = "ProductID",
            entityColumn = "ID"
    )
    public Product product;
}
