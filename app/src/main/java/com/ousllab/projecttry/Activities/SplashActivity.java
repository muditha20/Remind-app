package com.ousllab.projecttry.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.content.Intent;
import android.os.Handler;

import com.ousllab.projecttry.R;


public class SplashActivity extends AppCompatActivity {
    private static int Splash_Time_Out = 3000; // Delay time in milliseconds
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //hide action bar
        getSupportActionBar().hide();
        //full screen view
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //add splash screen activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, loginPage.class);
                startActivity(i);
                finish();
            }
        }, Splash_Time_Out);
        //End  splash activity
    }
}