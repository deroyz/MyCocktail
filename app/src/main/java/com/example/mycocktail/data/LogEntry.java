package com.example.mycocktail.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

//Room Data Model
@Entity(tableName = "log")

public class LogEntry {
    @PrimaryKey(autoGenerate = true)

    private int id = 0;

    private String name;
    private String comment;
    private double price;
    private float rating;

    private String photoPath;

    @ColumnInfo(name = "updated_at")

    private Date updatedAt;

    /*

    private Place place;

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
    */

    private Boolean isFavorite = false;
    private Boolean isFromGallery;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public Boolean getFromGallery() {
        return isFromGallery;
    }

    public void setFromGallery(Boolean fromGallery) {
        isFromGallery = fromGallery;
    }

}
