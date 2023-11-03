// Importing necessary packages
package com.ousllab.projecttry.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ousllab.projecttry.R;
import com.ousllab.projecttry.Adapters.TaskAdapter;

// Main activity class
public class MainActivity extends AppCompatActivity {
    TextView noTask;
    RecyclerView recyclerView;
    DatabaseHelper databaseHelper;
    // OnCreate method for creating activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing UI elements
        noTask=findViewById(R.id.tvNoTask);
        recyclerView=findViewById(R.id.recyclerView);
        databaseHelper=new DatabaseHelper(this);

        // Requesting permission for POST_NOTIFICATIONS
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.POST_NOTIFICATIONS},
                67);

        // Checking if there are any saved tasks in the database
        if (databaseHelper.getAllTasks().size()>0){

            // Creating adapter and setting up RecyclerView
            TaskAdapter taskAdapter=new TaskAdapter(this,databaseHelper.getAllTasks(),databaseHelper);
            recyclerView.setAdapter(taskAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }else {
            noTask.setVisibility(View.VISIBLE);
        }

        // Making the activity full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Initializing UI elements for adding tasks and opening settings
        ImageButton addTask = findViewById(R.id.add_icon_btn);
        ImageButton settings = findViewById(R.id.btnSettings);

        // Opening add task activity on clicking the button
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivity(intent);
            }
        });

        // Opening settings activity on clicking the button
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}