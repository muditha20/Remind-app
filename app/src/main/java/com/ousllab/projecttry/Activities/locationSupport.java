package com.ousllab.projecttry.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ousllab.projecttry.Adapters.TaskAdapterLocation;
import com.ousllab.projecttry.R;

public class locationSupport extends AppCompatActivity {
    RecyclerView recylerview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        //initialize widgets
        recylerview=findViewById(R.id.recylerview);
        TaskAdapterLocation taskAdapterLocation =new TaskAdapterLocation(this,new DatabaseHelper(this));
        recylerview.setAdapter(taskAdapterLocation);
        recylerview.setLayoutManager(new LinearLayoutManager(this));
    }
}