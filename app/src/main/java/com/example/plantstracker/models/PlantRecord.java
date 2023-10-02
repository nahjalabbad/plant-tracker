package com.example.plantstracker.models;

import android.content.ContentValues;

public class PlantRecord {
    private int id;
    private int plantId;
    private String date;

    public PlantRecord(int id, int plantId, String date) {
        this.id = id;
        this.plantId = plantId;
        this.date = date;
    }

    public PlantRecord(int plantId, String date) {
        this.plantId = plantId;
        this.date = date;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("plantId", plantId);
        values.put("date", date);
        return values;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlantId() {
        return plantId;
    }

    public void setPlantId(int plantId) {
        this.plantId = plantId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
