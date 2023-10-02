package com.example.plantstracker.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.plantstracker.service.PlantNotificationService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static void showToast(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static boolean checkMultiplePermissions(Context context, String[] permissionList) {
        for (String s : permissionList) {
            if (ContextCompat.checkSelfPermission(context, s) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    public static String getTimeDifferenceFromWateredDate(String wateredDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date wateredDateObj = sdf.parse(wateredDate);
            Date currentDate = new Date();

            long diffInMillis = currentDate.getTime() - wateredDateObj.getTime();
            long diffInSeconds = diffInMillis / 1000;
            long diffInMinutes = diffInSeconds / 60;
            long diffInHours = diffInMinutes / 60;
            long diffInDays = diffInHours / 24;

            long remainingHours = diffInHours % 24;

            String days;
            if (diffInDays > 1)
                days = " days ";
            else
                days = " day ";

            String hours;
            if (diffInDays > 1)
                hours = " hours ";
            else
                hours = " hour ";
            if (diffInDays == 0 && remainingHours == 0)
                return "Just Now";
            else
                return diffInDays + days + remainingHours + hours;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static boolean isDateSame(String specificDate){
//        specificDate = "2023-05-25 14:00:37";

        try {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            String specificDateOnly = specificDate.substring(0, 10);

            return currentDate.equals(specificDateOnly);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void startService(Context context){
        // service
        Intent serviceIntent = new Intent(context, PlantNotificationService.class);
        context.stopService(serviceIntent);
        context.startService(serviceIntent);
    }

    public static void stopService(Context context){
        // service
        Intent serviceIntent = new Intent(context, PlantNotificationService.class);
        context.stopService(serviceIntent);
    }

}
