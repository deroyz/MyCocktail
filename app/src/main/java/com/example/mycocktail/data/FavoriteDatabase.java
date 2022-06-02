package com.example.mycocktail.data;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FavoriteEntry.class}, version = 2, exportSchema = false)

public abstract class FavoriteDatabase extends RoomDatabase {

    private static final String LOG_TAG = FavoriteDatabase.class.getSimpleName();

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favoriteList";

    private static FavoriteDatabase sInstance;


    public static FavoriteDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {

                Log.e(LOG_TAG, "Creating new favorite database instance");

                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                                FavoriteDatabase.class, FavoriteDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();

            }
        }

        Log.e(LOG_TAG, "Getting the database instance");

        return sInstance;

    }

    public abstract FavoriteDao favoriteDao();

}
