package com.example.mycocktail.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import android.util.Log;

@Database(entities = {LogEntry.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)

public abstract class LogDatabase extends RoomDatabase {

    private static final String LOG_TAG = LogDatabase.class.getSimpleName();

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "loglist";

    private static LogDatabase sInstance;


    public static LogDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {

                Log.e(LOG_TAG, "Creating new database instance");

                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                                LogDatabase.class, LogDatabase.DATABASE_NAME)
                        .build();

            }
        }

        Log.e(LOG_TAG, "Getting the database instance");

        return sInstance;

    }

    public abstract LogDao logDao();

}
