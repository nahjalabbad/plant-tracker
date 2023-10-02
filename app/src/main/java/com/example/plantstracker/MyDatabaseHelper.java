package com.example.plantstracker;

import static com.example.plantstracker.utils.Constants.TYPE_INDOOR;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.plantstracker.models.Image;
import com.example.plantstracker.models.Plant;
import com.example.plantstracker.models.PlantRecord;
import com.example.plantstracker.models.User;
import com.example.plantstracker.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "myDatabase.db";
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Define database schema and create tables
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Constants.USERS_TABLE + "(id INTEGER PRIMARY KEY, name TEXT, email TEXT, password TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Constants.PLANTS_TABLE + "(id INTEGER PRIMARY KEY, userId INTEGER, name TEXT, image TEXT, howToTakeCare TEXT, howManyTimesToWater INTEGER, howManyTimesToChangeSoil INTEGER, isRequiredDirectSunLight INTEGER, type TEXT, wateredDate TEXT, changedSoilDate TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Constants.PLANT_RECORDS_TABLE + "(id INTEGER PRIMARY KEY, plantId INTEGER, date TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + Constants.IMAGES_TABLE + "(id INTEGER PRIMARY KEY, plantRecordId INTEGER, image TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade if needed
        db.execSQL("DROP TABLE IF EXISTS " + Constants.USERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.PLANTS_TABLE);
        onCreate(db);
    }

    public void insertAdmin(){
        // Insert data
        if (getUser("admin@gmail.com", "admin") == null) {
            deleteTable(Constants.USERS_TABLE);
            ArrayList<User> users = new ArrayList<>();
            users.add(new User("Admin", "admin@gmail.com", "admin"));
            for (int i = 0; i < users.size(); i++) {
                getWritableDatabase().insert(Constants.USERS_TABLE, null, users.get(i).toContentValues());
            }
        }
    }

    public boolean insertUser(User user) {
        long result = getWritableDatabase().insert(Constants.USERS_TABLE, null, new User(user.getName(), user.getEmail(), user.getPassword()).toContentValues());
        return result != -1; // Returns true if insertion was successful, false otherwise
    }

    public User getUser(String userEmail, String userPassword) {
        User user = null;
        String selection = "email = ? AND password = ?";
        String[] selectionArgs = {userEmail, userPassword};
        Cursor cursor = getReadableDatabase().query(Constants.USERS_TABLE, null, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex("name");
            int emailIndex = cursor.getColumnIndex("email");
            int passwordIndex = cursor.getColumnIndex("password");
            if (idIndex != -1 && nameIndex != -1 && passwordIndex != -1 && emailIndex != -1) {
                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String email = cursor.getString(emailIndex);
                String password = cursor.getString(passwordIndex);
                user = new User(id, name, email, password);
            }
        }
        cursor.close();
        return user;
    }

    public Boolean doesUserExists(String userEmail) {
        User user = null;
        String selection = "email = ?";
        String[] selectionArgs = {userEmail};
        Cursor cursor = getReadableDatabase().query(Constants.USERS_TABLE, null, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex("name");
            int emailIndex = cursor.getColumnIndex("email");
            int passwordIndex = cursor.getColumnIndex("password");
            if (idIndex != -1 && nameIndex != -1 && passwordIndex != -1 && emailIndex != -1) {
                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String email = cursor.getString(emailIndex);
                String password = cursor.getString(passwordIndex);
                user = new User(id, name, email, password);
            }
        }
        cursor.close();
        return user != null;
    }


    public void insertPlants(int userId){
        if (getAllPlants(userId).isEmpty()) {
            // Insert data
            deleteTable(Constants.PLANTS_TABLE);
            ArrayList<Plant> plants = new ArrayList<>();
            for (int i = 0; i < plants.size(); i++) {
                getWritableDatabase().insert(Constants.PLANTS_TABLE, null, plants.get(i).toContentValues());
            }
        }
    }

    public void insertPlant(Plant plant){
        getWritableDatabase().insert(Constants.PLANTS_TABLE, null, plant.toContentValues());
    }

    public void insertProgress(PlantRecord plantRecord, ArrayList<String> images){
        int plantRecordId = (int) getWritableDatabase().insert(Constants.PLANT_RECORDS_TABLE, null, plantRecord.toContentValues());
        for (int i = 0; i < images.size(); i++) {
            getWritableDatabase().insert(Constants.IMAGES_TABLE, null, new Image(plantRecordId, images.get(i)).toContentValues());
        }
    }

    public ArrayList<Plant> getAllPlants(int _userId) {
        ArrayList<Plant> plantArrayList = new ArrayList<>();
        String selection = "userId = ?";
        String[] selectionArgs = {String.valueOf(_userId)};
        Cursor cursor = getReadableDatabase().query(Constants.PLANTS_TABLE, null, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex("id");
            int userIdIndex = cursor.getColumnIndex("userId");
            int nameIndex = cursor.getColumnIndex("name");
            int imageIndex = cursor.getColumnIndex("image");
            int howToTakeCareIndex = cursor.getColumnIndex("howToTakeCare");
            int howManyTimesToWaterIndex = cursor.getColumnIndex("howManyTimesToWater");
            int howManyTimesToChangeSoilIndex = cursor.getColumnIndex("howManyTimesToChangeSoil");
            int isRequiredDirectSunLightIndex = cursor.getColumnIndex("isRequiredDirectSunLight");
            int typeIndex = cursor.getColumnIndex("type");
            int wateredDateIndex = cursor.getColumnIndex("wateredDate");
            int changedSoilDateIndex = cursor.getColumnIndex("changedSoilDate");
            if (idIndex != -1 && nameIndex != -1 && howToTakeCareIndex != -1 && howManyTimesToWaterIndex != -1 && typeIndex != -1 && imageIndex != -1 && isRequiredDirectSunLightIndex != -1) {
                int id = cursor.getInt(idIndex);
                int userId = cursor.getInt(userIdIndex);
                String name = cursor.getString(nameIndex);
                String image = cursor.getString(imageIndex);
                String howToTakeCare = cursor.getString(howToTakeCareIndex);
                int howManyTimesToWater = cursor.getInt(howManyTimesToWaterIndex);
                int howManyTimesToChangeSoil = cursor.getInt(howManyTimesToChangeSoilIndex);
                int isRequiredDirectSunLight = cursor.getInt(isRequiredDirectSunLightIndex);
                String type = cursor.getString(typeIndex);
                String wateredDate = cursor.getString(wateredDateIndex);
                String changedSoilDate = cursor.getString(changedSoilDateIndex);
                plantArrayList.add(new Plant(id, userId, name, image, howToTakeCare, howManyTimesToWater, howManyTimesToChangeSoil, isRequiredDirectSunLight, type, wateredDate, changedSoilDate));
            }
        }
        cursor.close();
        return plantArrayList;
    }

    /*private final MutableLiveData<List<Plant>> cartProductsLive = new MutableLiveData<>();

    public LiveData<List<Plant>> getCartProductsLive() {
        return cartProductsLive;
    }*/

    public void deleteTable(String tableName) {
        SQLiteDatabase db = getWritableDatabase();
        String deleteQuery = "DELETE FROM " + tableName + ";";
        db.execSQL(deleteQuery);
        db.close();
    }

    public Plant getProductById(int productId) {
        Plant product = null;
//        String[] projection = {"id", "userId", "name", "price", "image", "categoryId", "quantity"};
        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(productId)};
        Cursor cursor = getReadableDatabase().query(Constants.PLANTS_TABLE, null, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int userIdIndex = cursor.getColumnIndex("userId");
            int nameIndex = cursor.getColumnIndex("name");
            int imageIndex = cursor.getColumnIndex("image");
            int howToTakeCareIndex = cursor.getColumnIndex("howToTakeCare");
            int howManyTimesToWaterIndex = cursor.getColumnIndex("howManyTimesToWater");
            int howManyTimesToChangeSoilIndex = cursor.getColumnIndex("howManyTimesToChangeSoil");
            int isRequiredDirectSunLightIndex = cursor.getColumnIndex("isRequiredDirectSunLight");
            int typeIndex = cursor.getColumnIndex("type");
            int wateredDateIndex = cursor.getColumnIndex("wateredDate");
            int changedSoilDateIndex = cursor.getColumnIndex("changedSoilDate");
            if (idIndex != -1 && userIdIndex != -1 && nameIndex != -1 && imageIndex != -1 && howToTakeCareIndex != -1 && howManyTimesToWaterIndex != -1 && howManyTimesToChangeSoilIndex != -1 && isRequiredDirectSunLightIndex != -1 && typeIndex != -1) {
                int id = cursor.getInt(idIndex);
                int userId = cursor.getInt(userIdIndex);
                String name = cursor.getString(nameIndex);
                String image = cursor.getString(imageIndex);
                String howToTakeCare = cursor.getString(howToTakeCareIndex);
                int howManyTimesToWater = cursor.getInt(howManyTimesToWaterIndex);
                int howManyTimesToChangeSoil = cursor.getInt(howManyTimesToChangeSoilIndex);
                int isRequiredDirectSunLight = cursor.getInt(isRequiredDirectSunLightIndex);
                String type = cursor.getString(typeIndex);
                String wateredDate = cursor.getString(wateredDateIndex);
                String changedSoilDate = cursor.getString(changedSoilDateIndex);
                product = new Plant(id, userId, name, image, howToTakeCare, howManyTimesToWater, howManyTimesToChangeSoil, isRequiredDirectSunLight, type, wateredDate, changedSoilDate);
            }
        }
        cursor.close();
        return product;
    }

    public void updateWateredDateToCurrent(int productId) {
        ContentValues values = new ContentValues();
        values.put("wateredDate", getCurrentDateTime());

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(productId)};

        getWritableDatabase().update(Constants.PLANTS_TABLE, values, whereClause, whereArgs);
    }

    public void updateChangedSoilDateToCurrent(int productId) {
        ContentValues values = new ContentValues();
        values.put("changedSoilDate", getCurrentDateTime());

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(productId)};

        getWritableDatabase().update(Constants.PLANTS_TABLE, values, whereClause, whereArgs);
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    public PlantRecord getPlantRecordById(int productId) {
        PlantRecord plantRecord = null;
        String selection = "plantId = ?";
        String[] selectionArgs = {String.valueOf(productId)};
        Cursor cursor = getReadableDatabase().query(Constants.PLANT_RECORDS_TABLE, null, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int plantIdIndex = cursor.getColumnIndex("plantId");
            int dateIndex = cursor.getColumnIndex("date");
            if (idIndex != -1 && plantIdIndex != -1 && dateIndex != -1) {
                int id = cursor.getInt(idIndex);
                int plantId = cursor.getInt(plantIdIndex);
                String date = cursor.getString(dateIndex);
                plantRecord = new PlantRecord(id, plantId, date);
            }
        }
        cursor.close();
        return plantRecord;
    }

    public ArrayList<String> getImageById(int productId) {
        ArrayList<String> imageArrayList = new ArrayList<>();
        String selection = "plantRecordId = ?";
        String[] selectionArgs = {String.valueOf(productId)};
        Cursor cursor = getReadableDatabase().query(Constants.IMAGES_TABLE, null, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            int imageIndex = cursor.getColumnIndex("image");
            if (imageIndex != -1) {
                String image = cursor.getString(imageIndex);
                imageArrayList.add(image);
            }
        }
        cursor.close();
        return imageArrayList;
    }
}