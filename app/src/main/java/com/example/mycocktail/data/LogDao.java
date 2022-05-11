package com.example.mycocktail.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LogDao {

    @Insert
    void insertLog(LogEntry logEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateLog(LogEntry logEntry);

    @Delete
    void deleteLog(LogEntry logEntry);

    @Query("SELECT * FROM log ORDER BY updated_at")
    LiveData<List<LogEntry>> loadALLLogs();

    @Query("SELECT * FROM log WHERE id = :id")
    LiveData<LogEntry> loadTaskById(int id);


}
