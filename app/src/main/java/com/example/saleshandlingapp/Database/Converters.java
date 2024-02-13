package com.example.saleshandlingapp.Database;

import androidx.room.TypeConverter;

import com.example.saleshandlingapp.Enum.OrderStatus;
import com.example.saleshandlingapp.Enum.OrderType;

import java.time.LocalDateTime;

/**
 * Klasa zawierająca metody konwersji obsługujących specjalne typy danych w bazie Room.
 */
public class Converters {

    /**
     * Konwersuje wartość liczbową na enum OrderStatus.
     * @param value wartość do skonwertowania.
     * @return odpowiadający wartości enum OrderStatus.
     */
    @TypeConverter
    public OrderStatus IntToStatus(int value){
        switch (value){
            case 0: return OrderStatus.NOT_COMPLETED;
            case 1: return OrderStatus.COMPLETED;
            case 2: return OrderStatus.CANCELED;
            default:throw new IllegalArgumentException("Invalid enum value...");
        }
    }

    /**
     * Konwertuje enum OrderStatus na wartość liczbową.
     * @param status enum do skonwertowania.
     * @return odpowiadająca mu wartość liczbowa.
     */
    @TypeConverter
    public int StatusToInt(OrderStatus status){
        return status.ordinal();
    }

    /**
     * Konwertuje ciąg znaków na obiekt LocalDateTime.
     * @param date data w formie ciągu znaków.
     * @return obiekt LocalDateTime odpowiadający ciągowi znaków lub null.
     */
    @TypeConverter
    public static LocalDateTime fromString(String date) {
        return date == null ? null : LocalDateTime.parse(date);
    }

    /**
     * Konwertuje datę w formacie obiektu LocalDateTime na ciąg znaków.
     * @param date obiekt LocalDateTime.
     * @return data w formie ciągu znaków lub null.
     */
    @TypeConverter
    public static String timeToString(LocalDateTime date) {
        return date == null ? null : date.toString();
    }

    /**
     * Konwertuje enum OrderType na ciąg znaków opisujący typ zamówienia.
     * @param orderType enum OrderType do skonwertowania.
     * @return ciąg znaków opisujący typ zamówienia lub null.
     */
    @TypeConverter
    public static String OrderTypeToString(OrderType orderType){
        return orderType.getDescription();
    }

    /**
     * Konwertuje ciąg znaków opisujący typ zamówienia na enum OrderType.
     * @param description ciąg znaków opisujący typ zamówienia.
     * @return enum OrderType odpowiadający ciągowi znaków lub null.
     */
    @TypeConverter
    public static OrderType StringToOrderType(String description){
        return OrderType.fromDescription(description);
    }
}
