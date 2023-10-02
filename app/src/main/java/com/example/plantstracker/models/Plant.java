package com.example.plantstracker.models;

import android.content.ContentValues;

public class Plant {

    private int id;
    private int userId;
    private String name;
    private String image;
    private String howToTakeCare;
    private int howManyTimesToWater;
    private int howManyTimesToChangeSoil;
    private int isRequiredDirectSunLight;
    private String wateredDate;
    private String changedSoilDate;
    private String type;

    public Plant(int userId, String name, String image, String howToTakeCare, int howManyTimesToWater, int howManyTimesToChangeSoil, int isRequiredDirectSunLight, String type) {
        this.userId = userId;
        this.name = name;
        this.image = image;
        this.howToTakeCare = howToTakeCare;
        this.howManyTimesToWater = howManyTimesToWater;
        this.howManyTimesToChangeSoil = howManyTimesToChangeSoil;
        this.isRequiredDirectSunLight = isRequiredDirectSunLight;
        this.type = type;
    }

    public Plant(int id, int userId, String name, String image, String howToTakeCare, int howManyTimesToWater, int howManyTimesToChangeSoil, int isRequiredDirectSunLight, String type, String wateredDate, String changedSoilDate) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.image = image;
        this.howToTakeCare = howToTakeCare;
        this.howManyTimesToWater = howManyTimesToWater;
        this.howManyTimesToChangeSoil = howManyTimesToChangeSoil;
        this.isRequiredDirectSunLight = isRequiredDirectSunLight;
        this.type = type;
        this.wateredDate = wateredDate;
        this.changedSoilDate = changedSoilDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getIsRequiredDirectSunLight() {
        return isRequiredDirectSunLight;
    }

    public void setIsRequiredDirectSunLight(int isRequiredDirectSunLight) {
        this.isRequiredDirectSunLight = isRequiredDirectSunLight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getHowToTakeCare() {
        return howToTakeCare;
    }

    public void setHowToTakeCare(String howToTakeCare) {
        this.howToTakeCare = howToTakeCare;
    }

    public int getHowManyTimesToWater() {
        return howManyTimesToWater;
    }

    public void setHowManyTimesToWater(int howManyTimesToWater) {
        this.howManyTimesToWater = howManyTimesToWater;
    }

    public int getHowManyTimesToChangeSoil() {
        return howManyTimesToChangeSoil;
    }

    public void setHowManyTimesToChangeSoil(int howManyTimesToChangeSoil) {
        this.howManyTimesToChangeSoil = howManyTimesToChangeSoil;
    }

    public int getRequiredDirectSunLight() {
        return isRequiredDirectSunLight;
    }

    public void setRequiredDirectSunLight(int requiredDirectSunLight) {
        isRequiredDirectSunLight = requiredDirectSunLight;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWateredDate() {
        return wateredDate;
    }

    public void setWateredDate(String wateredDate) {
        this.wateredDate = wateredDate;
    }

    public String getChangedSoilDate() {
        return changedSoilDate;
    }

    public void setChangedSoilDate(String changedSoilDate) {
        this.changedSoilDate = changedSoilDate;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("userId", userId);
        values.put("name", name);
        values.put("image", image);
        values.put("howToTakeCare", howToTakeCare);
        values.put("howManyTimesToWater", howManyTimesToWater);
        values.put("howManyTimesToChangeSoil", howManyTimesToChangeSoil);
        values.put("isRequiredDirectSunLight", isRequiredDirectSunLight);
        values.put("type", type);
        values.put("wateredDate", wateredDate);
        values.put("changedSoilDate", changedSoilDate);
        return values;
    }
}