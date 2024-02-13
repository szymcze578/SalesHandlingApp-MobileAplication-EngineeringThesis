package com.example.saleshandlingapp.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Interfejs Data Access Object (DAO) definiujący operacje wykonywane na tabeli Clients.
 */
@Dao
public interface ClientDao {

    /**
     * Metoda pobierająca wszystkich klientów.
     * @return lista wszytskich klientów z bazy danych.
     */
    @Query("SELECT * FROM Clients")
    List<Client> getAllClients();

    /**
     * Dodaje nowych klientów do bazy danych.
     * @param clients klient do dodania.
     */
    @Insert
    void insertAll(Client ... clients);

    /**
     * Pobiera klienta z bazy danych na podstawie jego numeru telefonu.
     * @param number numer telefonu klienta.
     * @return klient o podanym numerze telfonu lub null, jeśli taki klient nie istnieje.
     */
    @Query("SELECT * FROM Clients WHERE Phonenumber == :number")
    Client getClientByPhoneNumber(String number);

    /**
     * Sprawdza, czy istnieje klient o podanym numerze telefonu.
     * @param number numer telefonu klienta.
     * @return true, jeśli klient o podanym numerze telefonu istnieje; false w przeciwnym razie.
     */
    @Query("SELECT EXISTS(SELECT * FROM Clients WHERE Phonenumber == :number)")
    boolean getClientByNumber(String number);

    /**
     * Pobiera klienta z bazy danych na podstawie jego numeru identyfikacyjnego.
     * @param clientID numer identyfikacyjny klienta.
     * @return klient o podanym numerze identyfikacyjnym lub null, jeśli taki klient nie istnieje.
     */
    @Query("SELECT * FROM Clients WHERE ID == :clientID")
    Client getClientByID(int clientID);

    /**
     * Aktualizuje dane klienta w bazie danych.
     * @param client klient do zaktualizowania.
     */
    @Update
    void updateClient(Client client);

}
