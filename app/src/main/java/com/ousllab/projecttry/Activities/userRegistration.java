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

public class userRegistration extends AppCompatActivity {

    EditText emailAddress,password;
    Button registerBtn;
    FirebaseAuth mAuth;
    TextView textViewLogin;

    public void onStart() {
        super.onStart();
        // Check if user is signed in and update load main screen.
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
        setContentView(R.layout.activity_user_registration);
        // Get an instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        //find views
        emailAddress = findViewById(R.id.emailAddress);
        password = findViewById(R.id.passwordEditText);
        registerBtn = findViewById(R.id.loginBtn);
        textViewLogin = findViewById(R.id.textViewLogin);

        //full screen view
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);




        //register button action
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, passwordTxt;
                email = emailAddress.getText().toString();
                passwordTxt = password.getText().toString();

                //check if user entered email
                if (TextUtils.isEmpty(email)){
                    emailAddress.setError("Please Enter Email");
                    return;
                } else if (TextUtils.isEmpty(passwordTxt)) { //check if user entered password
                    password.setError("Please Enter Password");
                    return;
                }

                //Register button action
                if (validatePassword()){
                    mAuth.createUserWithEmailAndPassword(email, passwordTxt)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //if registration successful show "Registration Sucessful"
                                    if (task.isSuccessful()) {
                                        Toast.makeText(userRegistration.this, "Registration Sucessful",
                                                Toast.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().signOut();
                                        startActivity(new Intent(userRegistration.this, loginPage.class));
                                    } else {
                                        validatePassword();
                                    }
                                }
                            });
                }
            }
        });

        //login button to loan login page
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),loginPage.class);
                startActivity(intent);
                finish();
            }
        });
    }

        /**
         * password validation password must meet the following requirements:
         * Contains at least one digit
         * Contains at least one lowercase letter
         * Contains at least one uppercase letter
         * Has a minimum length of 5 characters
        **/
    private boolean validatePassword() {
        String passwordInput =  password.getText().toString().trim();

        if (!passwordInput.matches( ".{5,}")) {
            //Toast.makeText(userRegistration.this, "Password should contain 8 characters", Toast.LENGTH_SHORT).show();
            password.setError("Password should contain 5 characters");
            return false;
        }
        else if (!passwordInput.matches(".*[a-z].*")) {
            //Toast.makeText(userRegistration.this, "Password should contain at least 1 lower case letter", Toast.LENGTH_SHORT).show();
            password.setError("Password must contain at least one lowercase letter");
            return false;
        }
        else if (!passwordInput.matches(".*[A-Z].*")) {
            //Toast.makeText(userRegistration.this, "Password should contain at least 1 upper case letter", Toast.LENGTH_SHORT).show();
            password.setError("Password must contain at least one uppercase letter");
            return false;
        }
        else if (!passwordInput.matches(".*[0-9].*")) {
            //Toast.makeText(userRegistration.this, "Password should contain at least 1 digit", Toast.LENGTH_SHORT).show();
            password.setError("Password must contain at least one digit");
            return false;
        }
        else {
            return true;
        }
    }
}