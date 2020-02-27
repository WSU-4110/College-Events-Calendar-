package com.example.campusconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.ProgressBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import android.text.TextUtils;


public class UserSignIn extends AppCompatActivity {

    private EditText email;
    private EditText password;
    ProgressBar progressBar;
    Button signin;
    Button gotosignup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_in);


        email= findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);
        progressBar = findViewById(R.id.progressBar2);


        signin= findViewById(R.id.btn_login);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           final String useremail=email.getText().toString().trim();
           final String userpassword=password.getText().toString().trim();

                if (TextUtils.isEmpty(useremail)) {
                    email.setError("Email is Required.");
                    return;
                }

                if (TextUtils.isEmpty(userpassword)) {
                    password.setError("Password is Required.");
                    return;
                }

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
