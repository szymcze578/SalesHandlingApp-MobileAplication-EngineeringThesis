package com.example.saleshandlingapp.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

/**
 * Klasa reprezentująca instancję bazy danych Room.
 */
@androidx.room.Database(entities = {Message.class, Product.class, Client.class,Order.class,OrderItem.class,CartItem.class},version = 1,exportSchema = false)
@TypeConverters(Converters.class)
public abstract class Database extends RoomDatabase {

    /**
     * Abstrakcyjna metoda zwracająca interfejs data access object dla encji Message.
     * @return data access object dla encji Message.
     */
    public abstract MessageDao messageDao();

    /**
     * Abstrakcyjna metoda zwracająca interfejs data access object dla encji Product.
     * @return data access object dla encji Product.
     */
    public abstract ProductDao productDao();

    /**
     * Abstrakcyjna metoda zwracająca interfejs data access object dla encji Client.
     * @return data access object dla encji Client.
     */
    public abstract ClientDao clientDao();

    /**
     * Abstrakcyjna metoda zwracająca interfejs data access object dla encji Order.
     * @return data access object dla encji Order.
     */
    public abstract OrderDao orderDao();

    /**
     * Abstrakcyjna metoda zwracająca interfejs data access object dla encji OrderItem.
     * @return data access object dla encji OrderItem.
     */
    public abstract OrderItemDao orderItemDao();

    /**
     * Abstrakcyjna metoda zwracająca interfejs data access object dla encji CartItem.
     * @return data access object dla encji CartItem.
     */
    public abstract CartItemDao cartItemDao();

    /**
     * Instancja bazy danych.
     */
    public static Database INSTANCE;

    /**
     * Metoda zwrająca instancję bazy danych. Wykorzystuje wzorzec projektowy Singleton.
     * @param context kontekst aplikacji.
     * @return instancja bazy danych Room.
     */
    public static Database getInstance(Context context){
        if(INSTANCE == null){
            synchronized (Database.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),Database.class,"Database")
                            .allowMainThreadQueries()
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            getInstance(context).productDao().insertAll(
                                                    new Product("egg","Jajka", 0.95, 0),
                                                    new Product("honey","Miód pszczeli", 20, 0),
                                                    new Product("hen","Kurczak", 50, 0),
                                                    new Product("potatoes","Ziemniaki",2.5,0),
                                                    new Product("tomato","Pomidory",5,0),
                                                    new Product("cucumber","Ogórki",6,0),
                                                    new Product("carrots","Marchewki",7,0)
                                            );
                                        }
                                    });
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
