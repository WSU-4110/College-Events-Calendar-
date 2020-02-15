package com.example.campusconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

public class UserSignUp extends AppCompatActivity {

    private EditText email;
    private EditText password;
    Button signin;
    Button goToCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);


        email= findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);

        signin= findViewById(R.id.btn_login);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call function to sign up or just do it directly from here
            }
        });

        goToCalendar= findViewById(R.id.signup);
        goToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendar();
            }
        });


    }

    public void openCalendar() {
        Intent intent = new Intent(this, View_Calendar.class);
        startActivity(intent);
    }
}
