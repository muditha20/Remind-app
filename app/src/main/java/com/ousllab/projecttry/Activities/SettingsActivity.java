package com.ousllab.projecttry.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.ousllab.projecttry.R;
import com.ousllab.projecttry.Services.LocationTrackerService;

public class SettingsActivity extends AppCompatActivity {
    private boolean isServiceRunning(Class<?> serviceClass) {
        // Get the activity manager for the device
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        // Loop through all running services on the device
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            // Check if the service class name matches the given service class name
            if (serviceClass.getName().equals(service.service.getClassName())) {
                // Return true if the service is running
                return true;
            }
        }
        // Return false if the service is not running
        return false;
    }

    // Initialize DatabaseHelper object
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //full screen view
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button help = findViewById(R.id.btnHelp);
        Button aboutUs = findViewById(R.id.btnAboutUs);
        Button terms = findViewById(R.id.btnTerms);
        Button contactUs = findViewById(R.id.contactUs);
        Button share = findViewById(R.id.btnShare);
        Button btnStartService = findViewById(R.id.btnStartService);
        Button btnSignOut = findViewById(R.id.btnSignOutinSetting);

            // Initialize DatabaseHelper object
        databaseHelper=new DatabaseHelper(SettingsActivity.this);

            // Check if location permissions have been granted, if not, request them
        if (ContextCompat.checkSelfPermission(SettingsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED&ContextCompat.checkSelfPermission(SettingsActivity.this,
                Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS) != PackageManager.PERMISSION_GRANTED) {

            // Request location permissions
            ActivityCompat.requestPermissions(SettingsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS},
                    67);
        }

        // Check if LocationTrackerService is currently running
        if (isServiceRunning(LocationTrackerService.class)){
            btnStartService.setText("Stop Task Service");
        }else {
            btnStartService.setText("Start Task Service");
        }

        // Set up listener for button to start/stop LocationTrackerService
        btnStartService.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
        // Check if location permissions have been granted, if not, request them
                if (ContextCompat.checkSelfPermission(SettingsActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED&ContextCompat.checkSelfPermission(SettingsActivity.this,
                        Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS) != PackageManager.PERMISSION_GRANTED) {

                    // Request location permissions
                    ActivityCompat.requestPermissions(SettingsActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS},
                            67);
                } else {
                    // Create Intent for LocationTrackerService
                    Intent intent=new Intent(SettingsActivity.this,LocationTrackerService.class);

                    // If LocationTrackerService is currently running, stop it and update button text
                    if (isServiceRunning(LocationTrackerService.class)){
                        stopService(intent);
                        btnStartService.setText("Start Task Service");
                    }else {
                        // If no tasks have been added yet, show toast message and do not start LocationTrackerService
                        if (databaseHelper.getAllTasks().size()<1){
                            Toast.makeText(SettingsActivity.this, "You haven't added any tasks yet!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // Start LocationTrackerService and update button text
                        startService(intent);
                        btnStartService.setText("Stop Task Service");
                    }
                }
            }
        });
        //help window
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });

        //help  window end


        //about us window
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        //about us window end


        //terms window
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, TermsActivity.class);
                startActivity(intent);
            }
        });

        //terms us window end


        //contact window
        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ContactUsActivity.class);
                startActivity(intent);
            }
        });

        //contact us window end


        //share window
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ShareActivity.class);
                startActivity(intent);
            }
        });

        //share us window end

        //share window
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, signOut.class);
                startActivity(intent);
            }
        });

        //share us window end
    }
}