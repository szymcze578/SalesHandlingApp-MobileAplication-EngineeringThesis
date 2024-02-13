package com.example.saleshandlingapp.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.saleshandlingapp.Database.Message;

import java.util.List;

/**
 * Interfejs Data Access Object (DAO) definiujący operacje wykonywane na tabeli Messages.
 */
@Dao
public interface MessageDao {
    /**
     * Metoda pobierająca wszystkie wiadomości.
     * @return lista wszytskich wiadomości z bazy danych.
     */
    @Query("Select * FROM Messages")
    List<Message> getAllMesses();

    /**
     * Dodaje nowe wiadomości do bazy danych.
     * @param message wiadomość do dodania.
     */
    @Insert
    void insertAll(Message ... message);

    /**
     * Pobiera wiadomość z bazy danych na podstawie jej numeru identyfikacyjnego.
     * @param id numer identyfikacyjny wiadomości.
     * @return wiadomość o podanym numerze identyfikacyjnym lub null, jeśli taka wiadomość nie istnieje.
     */
    @Query("Select * FROM Messages WHERE ID = :id")
    Message getMessageByID(int id);

    /**
     * Usuwa wiadomość z bazy danych na podstawie jej numeru identyfikacyjnego.
     * @param id numer identyfikacyjny wiadomości do usunięcia.
     */
    @Query("DELETE FROM MESSAGES WHERE ID =:id")
    void deleteMessage(int id);
}
