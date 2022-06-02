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
public interface FavoriteDao {

    @Insert
    void insertFavorite(FavoriteEntry favoriteEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavorite(FavoriteEntry favoriteEntry);

    @Delete
    void deleteFavorite(FavoriteEntry favoriteEntry);

    @Query("SELECT * FROM favorite ORDER BY idDrink")
    List<FavoriteEntry> loadAllFavorites();

    @Query("SELECT * FROM favorite WHERE idDrink = :id")
    FavoriteEntry loadFavoriteById(String id);

    @Query("DELETE FROM favorite WHERE idDrink = :id")
    void deleteByDrinkId(String id);

    @Query("DELETE FROM favorite")
    void deleteAllFavorite();
}
