package com.ousllab.projecttry.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ousllab.projecttry.Activities.locationSupport;
import com.ousllab.projecttry.Activities.MainActivity;
import com.ousllab.projecttry.Configs;
import com.ousllab.projecttry.Activities.DatabaseHelper;
import com.ousllab.projecttry.Model.TaskModel;
import com.ousllab.projecttry.R;

import java.util.ArrayList;
import java.util.List;

public class LocationTrackerService extends Service {
    double userLong, userLat;
    FusedLocationProviderClient fusedLocationProviderClient;
    List<TaskModel> taskModelArrayList;
    Handler handler = new Handler();
    Runnable runnable;
    DatabaseHelper databaseHelper;

    public static ArrayList<TaskModel> locationModelArrayList;

    @Override
    public void onCreate() {
        // Create notification like as Build Version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground();
        } else {
            // Handle notification for older versions
        }
        super.onCreate();
    }

    /* Create custom notification */
    private void startMyOwnForeground() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 45;
        String channelId = "Location Tracker";
        String channelName = "Location Tracker";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notify_layout_charger);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 3131, intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        contentView.setOnClickPendingIntent(R.id.cons, pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            mChannel.setLightColor(Color.BLACK);
            mChannel.enableLights(true);
            mChannel.setVibrationPattern(null);
            mChannel.setSound(null, null);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setCustomContentView(contentView)
                .setVibrate(null)
                .setSound(null)
                .setDefaults(Color.BLACK)
                .setContentIntent(pendingIntent) // Update the intent here if needed
                .build();

        startForeground(notificationId, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Show notification
        startMyOwnForeground();

        locationModelArrayList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);
        taskModelArrayList = databaseHelper.getAllTasks();

        // Get user's current location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        runnable = new Runnable() {
            @Override
            public void run() {
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            userLat = location.getLatitude();
                            userLong = location.getLongitude();

                            for (int i = taskModelArrayList.size() - 1; i >= 0; i--) {
                                double dist = distance(taskModelArrayList.get(i).getLatitude(),
                                        taskModelArrayList.get(i).getLongitude(), userLat,
                                        userLong);
                                if (dist <= Configs.DISTANCE) {
                                    locationModelArrayList.add(taskModelArrayList.get(i));
                                    taskModelArrayList.remove(i);
                                    if (locationModelArrayList.size() > 0) {
                                        Intent mainIntent =
                                                new Intent(LocationTrackerService.this, locationSupport.class);
                                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        PendingIntent pendingIntent =
                                                PendingIntent.getActivity(LocationTrackerService.this, 8965, mainIntent,
                                                        PendingIntent.FLAG_IMMUTABLE);

                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(LocationTrackerService.this, "Location " +
                                                "Tracker")
                                                .setSmallIcon(R.drawable.baseline_notifications_24)
                                                .setContentTitle("There is a task near you")
                                                .setContentText("Click here and see the details")
                                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                .setContentIntent(pendingIntent)
                                                .setAutoCancel(true);

                                        NotificationManagerCompat notificationManager =
                                                NotificationManagerCompat.from(LocationTrackerService.this);
                                        notificationManager.notify(86598, builder.build());
                                    }
                                }
                            }

                            handler.postDelayed(runnable, Configs.INTERVAL);
                        }
                    }
                });
            }
        };

        handler.postDelayed(runnable, Configs.INTERVAL);
        return START_NOT_STICKY;
    }

    // Calculate distance
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.60934;
        return dist;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service if needed
        return null;
    }
}
