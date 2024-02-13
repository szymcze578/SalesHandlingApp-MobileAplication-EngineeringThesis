package com.example.saleshandlingapp.Database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

/**
 * Klasa reprezentująca pojedynczy rodzaj produktu dostępny na magazynie.
 * Implementuje interfejs Parcelable, który umożliwia przenoszenie obiektów typu Product między
 * komponentami w systemie Android dzięki czemu mogą być przekazywane między aktywnościami i fragmentami.
 *
 */
@Entity(tableName = "Products")
public class Product implements Parcelable {

    /**
     * Unikalny identyfikator produktu generowany automatycznie.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    private int ID;

    /**
     * Nazwa przypisanego obrazu wizualizującego produkt.
     */
    @ColumnInfo(name = "Image")
    private String imageName;

    /**
     * Nazwa produktu.
     */
    @ColumnInfo(name = "Name")
    private String productName;

    /**
     * Cena jednostkowa produktu za jedną sztukę bądź jeden kilogram.
     */
    @ColumnInfo(name = "Price(unit/kg)")
    private double price;

    /**
     * Ilość produktu dostępna na magazynie.
     */
    @ColumnInfo(name = "Amount")
    private int amount;


    /**
     * Pobiera identyfikator produktu.
     * @return identyfikator produktu.
     */
    public int getID() {
        return ID;
    }

    /**
     * Ustawia identyfikator produktu.
     * @param ID identfikator produktu.
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Pobiera nazwę produktu.
     * @return nazwa produktu.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Ustawia nazwę produktu.
     * @param productName nazwa produktu.
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Pobiera cenę jednostkową za dany produkt.
     * @return cena jednostkowa produktu.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Ustawia cenę jednostkową produktu.
     * @param price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Pobiera ilość produktu dostępną na magazynie.
     * @return ilość produktu na magazynie.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Ustawia ilość produktu dostęną na magazynie.
     * @param amount ilość produktu.
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Pobiera nazwę do pliku obrazu wizualizującego produkt.
     * @return nazwa pliku obrazu.
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * Ustawia nazwę pliku obrazu wizualizującego produkt.
     * @param imageName nazwa pliku obrazu.
     */
    public void setImage(String imageName) {
        this.imageName = imageName;
    }

    /**
     * Konstruktor klasy Product.
     * @param imageName nazwa obrazu wizualizującego produkt.
     * @param productName nazwa produktu.
     * @param price cena jednostkowa produktu.
     * @param amount ilość produktu na magazynie.
     */
    public Product(String imageName, String productName, double price, int amount) {
        this.imageName = imageName;
        this.productName = productName;
        this.price = price;
        this.amount = amount;
    }

    /**
     * Metoda zmniejszająca ilość produktu na magazynie o określoną wartość.
     * @param amount ilość produktu do odjęcia.
     */
    public void decreaseAmount(int amount){
        this.amount -= amount;
    }

    /**
     * Metoda zwiększająca ilość produktu na magazynie o określoną wartość.
     * @param amount ilość produktu do dodania.
     */
    public void increaseAmount(int amount){
        this.amount +=amount;
    }

    /**
     * Konstruktor chroniony, używany do odtworzenia obiektu Product z obiektu Parcel.
     * @param in obiekt Parcel, z którego odczytywane są dane.
     */
    protected Product(Parcel in) {
        ID = in.readInt();
        imageName = in.readString();
        productName = in.readString();
        price = in.readDouble();
        amount = in.readInt();
    }

    /**
     * Metoda z interfejsu Parcelable.
     * @return zawsze zwraca 0 ze względu na brak wykorzystania specjalnych flag.
     */
    @Override
    public int describeContents() {
        return 0;
    }


    /**
     * Metoda writeToParcel zapisuje stan obiektu Product do obiektu Parcel.
     * @param dest   obiekt Parcel, do którego zapisujemy dane.
     * @param flags  dodatkowe flagi kontrolujące sposób zapisu danych.
     */
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(imageName);
        dest.writeString(productName);
        dest.writeDouble(price);
        dest.writeInt(amount);
    }

    /**
     * Metoda statyczna obliczająca wartość całkowitą listy produktów na podstawie ich ilości i cen jednostkowych.
     * @param productList lista produktów.
     * @return wartość całkowita listy produktów.
     */
    public static double calculateValue(List<Product> productList){
        double value = 0;
        for (Product p : productList){
            value += p.getAmount()*p.getPrice();
        }
        return value;
    }

}
