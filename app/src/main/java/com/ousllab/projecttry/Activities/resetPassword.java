package com.ousllab.projecttry.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.ousllab.projecttry.R;

public class resetPassword extends AppCompatActivity {  
    
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        //full screen view
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Button resetPassword = findViewById(R.id.btnPasswordReset);
        EditText passwordResetEmailAddress = findViewById(R.id.passwordResetEmailAddress);

        //password reset button action
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = passwordResetEmailAddress.getText().toString();
                if (email.isEmpty()==true){
                    Toast.makeText(resetPassword.this, "Please enter correct email ", Toast.LENGTH_SHORT).show();
                }else{
                    resetPassword();
                }
            }
        });
    }
    //send password reset link to email
    private void resetPassword(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(resetPassword.this, "Password reset link sent to your email...", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(resetPassword.this, loginPage.class));
                        }else {
                            Toast.makeText(resetPassword.this, "Please check your email again..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}