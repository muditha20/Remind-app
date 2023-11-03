package com.ousllab.projecttry.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ousllab.projecttry.R;

public class loginPage extends AppCompatActivity {

    EditText emailAddress,passwordEditText;
    Button loginBtn;
    FirebaseAuth mAuth;
    TextView textViewRegister,textViewPasswordReset;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        mAuth = FirebaseAuth.getInstance();
        textViewRegister = findViewById(R.id.textViewRegister);

        loginBtn = findViewById(R.id.loginBtn);
        emailAddress = findViewById(R.id.emailAddress);
        passwordEditText = findViewById(R.id.passwordEditText);
        textViewPasswordReset = findViewById(R.id.textViewPasswordReset);

        //full screen view
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //login button activity
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, passwordTxt;
                email = emailAddress.getText().toString();
                passwordTxt = passwordEditText.getText().toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(loginPage.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(passwordTxt)){
                    Toast.makeText(loginPage.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, passwordTxt)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If the sign-in attempt is successful
                                if (task.isSuccessful()) {
                                    //Display logged successfully toast message
                                    Toast.makeText(loginPage.this, "You logged successfully",
                                            Toast.LENGTH_SHORT).show();

                                    // Create a new intent to launch the MainActivity
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    //remove the current activity from the activity stack
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(loginPage.this, "Please check your credential again",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });

        //Password reset button
        textViewPasswordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),resetPassword.class);
                startActivity(intent);
                finish();
            }
        });

        //User register button
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), userRegistration.class);
                startActivity(intent);
                finish();
            }
        });

    }
}