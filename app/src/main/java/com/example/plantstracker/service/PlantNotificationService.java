package com.example.plantstracker.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.example.plantstracker.MyDatabaseHelper;
import com.example.plantstracker.R;
import com.example.plantstracker.activities.MainActivity;
import com.example.plantstracker.models.Plant;
import com.example.plantstracker.utils.Session;
import com.example.plantstracker.utils.Utils;

import java.util.ArrayList;

public class PlantNotificationService extends Service {

    private static final String CHANNEL_ID = "plant_notification_channel";

    private MyDatabaseHelper databaseHelper;
    private Session session;
    private Handler handler;
    private Runnable runnable;

    @Override
    public void onCreate() {
        super.onCreate();
        databaseHelper = new MyDatabaseHelper(this);
        session = new Session(this);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                checkPlantRecords();
                handler.postDelayed(this, 86400000);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.post(runnable);
        return START_STICKY;
    }

    private void checkPlantRecords() {
        ArrayList<Plant> plants = databaseHelper.getAllPlants(session.getUserId());
        for (int i = 0; i < plants.size(); i++) {
            if (!Utils.isDateSame(plants.get(i).getWateredDate())){
                String notificationMessage = "Water your plant '" + plants.get(i).getName() + "' today!";
                showNotification(notificationMessage);
            }
            if (!Utils.isDateSame(plants.get(i).getChangedSoilDate())){
                String notificationMessage = "Change the soil of your plant '" + plants.get(i).getName() + "' today!";
                showNotification(notificationMessage);
            }
        }
    }

    public void showNotification(String notificationMessage){
        int notificationId = (int) System.currentTimeMillis();
        // Create a notification with the message
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_plant)
                .setContentTitle("Plant Care Reminder")
                .setContentText(notificationMessage)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationMessage))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // Create a pending intent to launch the app when the notification is clicked
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);

        // Show the notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            // Create a notification channel for Android Oreo and above
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Plant Care Channel", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            Notification notification = builder.build();
            notificationManager.notify(notificationId, notification);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }
}
