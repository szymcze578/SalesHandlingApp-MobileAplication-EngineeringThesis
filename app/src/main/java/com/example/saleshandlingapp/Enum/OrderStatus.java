package com.example.saleshandlingapp.Enum;

/**
 * Typ wyliczeniowy reprezentujący różne stany zlecenia.
 * <ul>
 *   <li>{@link #NOT_COMPLETED} - zlecenie nie zostało jeszcze zrealizowane.</li>
 *   <li>{@link #COMPLETED} - zlecenie zostało zrealizowane.</li>
 *   <li>{@link #CANCELED} - zlecenie zostało anulowane.</li>
 * </ul>
 */
public enum OrderStatus {
    NOT_COMPLETED,
    COMPLETED,
    CANCELED
}
