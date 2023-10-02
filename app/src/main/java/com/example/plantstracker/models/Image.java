package com.example.plantstracker.models;

import android.content.ContentValues;

public class Image {
    int id;
    int plantRecordId;
    String image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlantRecordId() {
        return plantRecordId;
    }

    public void setPlantRecordId(int plantRecordId) {
        this.plantRecordId = plantRecordId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Image(int id, int plantRecordId, String image) {
        this.id = id;
        this.plantRecordId = plantRecordId;
        this.image = image;
    }

    public Image(int plantRecordId, String image) {
        this.plantRecordId = plantRecordId;
        this.image = image;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("plantRecordId", plantRecordId);
        values.put("image", image);
        return values;
    }
}
