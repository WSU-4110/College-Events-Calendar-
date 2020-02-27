package com.example.campusconnect;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.campusconnect.UI.Authentication.signIn;
import com.example.campusconnect.UI.MainPage.home;
import com.google.firebase.auth.FirebaseAuth;


public class splashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        int SPLASH_TIME_OUT = 4000; //millisecond
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // User Login Check
                if (FirebaseAuth.getInstance().getCurrentUser() == null)
                    startActivity(new Intent(splashScreen.this, signIn.class));
                else
                    startActivity(new Intent(splashScreen.this, home.class));
            }
        }, SPLASH_TIME_OUT);
    }
}
