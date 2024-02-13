package com.example.saleshandlingapp.NotificationService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.saleshandlingapp.R;


public class NotificationSender {


    private static final String CHANNEL_ID = "CHANNEL_ID_NOTIFICATION";


    public static void makeNotification(Context context){
        NotificationCompat.Builder builder  = new NotificationCompat.Builder(context.getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Nowe zamówienie!")
                .setContentText("Przechwycono nowe zamówienie")
                .setStyle(new NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        createNotificationChannel(context);
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.notify(0,builder.build());
    }


    private static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
            if(notificationChannel == null) {

                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                notificationChannel = new NotificationChannel(CHANNEL_ID, "ChannelID", importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
    }

}
