package com.example.saleshandlingapp.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Clients")
public class Client {

    /**
     * Unikalny identyfikator klienta generowany automatycznie.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID") private int ID;
    /**
     * Imię klienta.
     */
    @ColumnInfo(name = "Name")private String name;
    /**
     * Nazwisko klienta.
     */
    @ColumnInfo(name = "Surname")private String surname;
    /**
     * Numer telefonu klienta.
     */
    @ColumnInfo(name = "Phonenumber")private String phoneNumber;
    /**
     * Miasto zamieszkania klienta.
     */
    @ColumnInfo(name = "City")private String city;
    /**
     * Adres zamieszkania klienta.
     */
    @ColumnInfo(name = "Address")private String address;

    /**
     * Konstruktor klasy Client.
     * @param name        imię klienta.
     * @param surname     nazwisko klienta.
     * @param phoneNumber numer telefonu klienta.
     * @param city        miasto zamieszkania klienta.
     * @param address     adres zamieszkania klienta.
     */
    public Client(String name, String surname, String phoneNumber, String city, String address) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.address = address;
    }

    /**
     * Pobiera identyfikator klienta.
     * @return identyfikator klienta.
     */
    public int getID() {
        return ID;
    }

    /**
     * Ustawia identifikator klienta.
     * @param ID identufikator klienta.
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Pobiera imię klienta.
     * @return imię klienta.
     */
    public String getName() {
        return name;
    }

    /**
     * Ustawia imię klienta.
     * @param name imię klienta.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Pobiera nazwisko klienta.
     * @return nazwisko klienta.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Ustawia nazwisko klienta.
     * @param surname nazwisko klienta.
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Pobiera numer telefonu klienta.
     * @return numer telefonu klienta.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Ustawia numer telefonu klienta.
     * @param phoneNumber numer telefonu klienta.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Pobiera adres zamieszkania klienta.
     * @return miasto zamieszkania klienta.
     */
    public String getCity() {
        return city;
    }

    /**
     * Ustawia miasto zamieszkania klienta.
     * @param city miasto zamieszkania klienta.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Pobiera adres zamieszkania klienta.
     * @return adres zamieszkania klienta
     */
    public String getAddress() {
        return address;
    }

    /**
     * Ustawia adres zamieszkania klienta.
     * @param address adres zamieszkania klienta.
     */
    public void setAddress(String address) {
        this.address = address;
    }
}
