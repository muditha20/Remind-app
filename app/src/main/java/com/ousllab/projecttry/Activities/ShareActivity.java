package com.ousllab.projecttry.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.ousllab.projecttry.R;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        //full screen view
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

// Find the share button and set a click listener
        Button shareButton = findViewById(R.id.btnShareApp);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            // Create an intent to share the app
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out my app!"); // Set the subject of the share message
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Download this amazing app: https://plastore.com/remind@app"); // Set the message body
                startActivity(Intent.createChooser(shareIntent, "Share my app")); // Start the intent to share and show the chooser dialog
            }
        });






    }
}