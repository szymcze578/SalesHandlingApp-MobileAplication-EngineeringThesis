package com.example.saleshandlingapp.Enum;

/**
 * Typ wyliczeniowy reprezentujący różne rodzaje zleceń.
 * <ul>
 *   <li>{@link #ORDER} - zlecenie kupna produktów przez klienta.</li>
 *   <li>{@link #DELIVERY} - zlecenie kupna i odbiory produktów od klienta.</li>
 * </ul>
 */
public enum OrderType {
    /**
     * Zlecenie kupna produktów.
     */
    ORDER("ZAMÓWIENIE"),
    /**
     * Zlecenie sprzedaży produktów (odbiór produktów od klienta).
     */
    DELIVERY ("DOSTAWA");

    /**
     * Opis typu zamówienia.
     */
    private final String description;

    /**
     * Konstruktor typu wyliczeniowego OrderType.
     * @param description
     */
    OrderType(String description) {
        this.description = description;
    }

    /**
     * Pobiera opis typu zlecenia.
     * @return opis typu zlecenia.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Zwraca wartość typu wyliczeniowego na podstawie jego opisu.
     * @param description opis rodzaju zlecenia.
     * @return typ wyliczeniowy OrderType odpowiadający przekazanemu opisowi lub null, jeśli nie zostanie znaleziony.
     */
    public static OrderType fromDescription(String description){
        OrderType[] types = OrderType.values();
        for(OrderType orderType : types){
            if(orderType.getDescription().equals(description))
                return orderType;
        }
        return null;
    }
}
