package com.ousllab.projecttry.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ousllab.projecttry.R;

public class AddTaskActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Location request code
    private static final int PERMISSIONS_REQUEST_LOCATION = 100;

    // Variables for storing latitude and longitude values
    double lat, longi;
    double longitude, latitude;

    // Selected location
    private LatLng mSelectedLocation;

    // Google Map instance
    private GoogleMap mMap;

    // Database Helper instance
    DatabaseHelper databaseHelper;

    // EditText instance for entering task details
    EditText editTask;

    private FusedLocationProviderClient mFusedLocationClient;

    // This method is called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task_layout);

        editTask = findViewById(R.id.editTask);
        databaseHelper = new DatabaseHelper(AddTaskActivity.this);

        //full screen view
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // check for location permissions
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // request permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_LOCATION);
        } else {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            // get user's current location
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        // make nearby search request
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
                        }
                    }
                }
            });
        }

        // home button activity start
        ImageButton addTask = findViewById(R.id.homebtn);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTaskActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        // home button activity end

        //save button action
        findViewById(R.id.btnAddTask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedLocation != null & editTask.getText().toString().length() > 1) {
                    databaseHelper.insertTask(editTask.getText().toString(),
                            mSelectedLocation.latitude, mSelectedLocation.longitude);
                    Toast.makeText(AddTaskActivity.this, "The task has been saved. You are being redirected to the homepage", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddTaskActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(AddTaskActivity.this, "Please fill in all the information!", Toast.LENGTH_SHORT).show();
                }
            }
        });

            // Get the proximity sensor from the sensor manager
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

            // Create a proximity sensor listener
        SensorEventListener proximitySensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                    float distance = event.values[0];
                    if (distance == 0) {
                        // The device is face-down, simulate a button click
                        findViewById(R.id.btnAddTask).performClick();

                        // Unregister the proximity sensor listener
                        sensorManager.unregisterListener(this);
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // Do nothing
            }
        };
            // Register the proximity sensor listener
                    sensorManager.registerListener(proximitySensorListener, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);

            // Show a message to the user
                    Toast.makeText(AddTaskActivity.this, "Please place your device face-down to save the task", Toast.LENGTH_LONG).show();
                }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // Call the superclass method
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Check if the request code is for location permissions
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {
                // If the permission is granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Get the map fragment and request the map
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(this);

                    // Get the user's current location
                    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                    mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                // make nearby search request
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
                                }
                            }
                        }
                    });
                } else {
                    // If permission is not granted, finish the app and show a toast message
                    finish();
                }
                return;
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            // Request location permission if it hasn't been granted yet
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

            // Enable showing the user's current location on the map
        mMap.setMyLocationEnabled(true);

            // Handle map click events to add a marker
        mMap.setOnMapClickListener(latLng -> {
            mMap.clear();
            MarkerOptions markerOptions = new MarkerOptions().position(latLng);
            mMap.addMarker(markerOptions);
            mSelectedLocation = latLng;
            Toast.makeText(AddTaskActivity.this, "Selected Location: " + latLng.toString(),
                    Toast.LENGTH_SHORT).show();
        });

        //AIzaSyAWiJN-G91OhUQ2CIzaJqfL2TwVPNd2GsE api key worked

            // Set up the user's current location to be shown on the map
        try {
            if (!TextUtils.isEmpty(getString(R.string.google_maps_api_key))) {
                String apiKey = getString(R.string.google_maps_api_key);
                MapsInitializer.initialize(this.getApplicationContext());

                // Show user's current location
                mFusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                    if (location != null) {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
                        mMap.animateCamera(cameraUpdate);
                    }
                });
            } else {
                Toast.makeText(this, "Google maps not available", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
