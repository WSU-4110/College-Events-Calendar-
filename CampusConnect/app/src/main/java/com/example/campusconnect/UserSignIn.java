package com.example.campusconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

public class UserSignIn extends AppCompatActivity {

    private EditText email;
    private EditText password;
    Button signin;
    Button gotosignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_in);


        email= findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);

        signin= findViewById(R.id.btn_login);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call function to sign in or just do it directly from here
            }
        });

        gotosignup= findViewById(R.id.signup);
        gotosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserSignUp();
            }
        });


    }

    public void openUserSignUp() {
        Intent intent = new Intent(this, UserSignUp.class);
        startActivity(intent);
    }
}
