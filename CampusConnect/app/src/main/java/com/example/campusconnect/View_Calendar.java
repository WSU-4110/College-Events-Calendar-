package com.example.campusconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class View_Calendar extends AppCompatActivity {

    Button button_ReturnToHomepage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_calendar);

        button_ReturnToHomepage = findViewById(R.id.returnto_main);             // Button redundant as Android includes back button on lower toolbar
        button_ReturnToHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backTo_HomeScreen();
            }
        });
    }

    public void backTo_HomeScreen(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}//end [ CLASS: View_Calendar ]