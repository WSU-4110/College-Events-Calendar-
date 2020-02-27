package com.example.campusconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.ProgressBar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import android.text.TextUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.widget.Toast;
import androidx.annotation.NonNull;

public class UserSignUp extends AppCompatActivity {

    private EditText emailText;
    private EditText passwordText;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    Button signin;
    Button goToCalendar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);


        emailText= findViewById(R.id.input_email);
        passwordText = findViewById(R.id.input_password);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        signin= findViewById(R.id.btn_login);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailText.getText().toString().trim();
                String password = passwordText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    emailText.setError("Email is Required.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    passwordText.setError("Password is Required.");
                    return;
                }



            }
        });

        goToCalendar = findViewById(R.id.signup);
        goToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), View_Calendar.class));
            }
        });
    }
}



