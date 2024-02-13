package com.example.saleshandlingapp.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

/**
 * Klasa reprezentująca wiadomość w systemie.
 */
@Entity(tableName = "Messages")
public class Message {

    /**
     * Unikalny numer identyfikacyjny wiadomości, generowany automatycznie.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    public int ID;
    /**
     * Treść wiadomości.
     */
    @ColumnInfo(name = "Message")
    public String message;

    /**
     * Numer telefonu nadawcy.
     */
    @ColumnInfo(name = "Number")
    public String number;

    /**
     * Data i czas wysłania wiadomości.
     */
    @ColumnInfo( name = "Date")
    public LocalDateTime date;

    /**
     * Konstruktor klasy message.
     * @param message treść wiadomości.
     * @param number numer telefonu nadawcy.
     * @param date data i czas wysłania wiadomości.
     */
    public Message(String message, String number, LocalDateTime date) {
        this.message = message;
        this.number = number;
        this.date = date;
    }
}
