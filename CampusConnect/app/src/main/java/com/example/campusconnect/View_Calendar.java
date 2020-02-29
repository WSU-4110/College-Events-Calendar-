//------------------------------------------------------------------------------------------//
//                                                                                          //
// Consult following link on Source Options and offline data usage                          //
// https://firebase.google.com/docs/firestore/query-data/get-data                           //
//                                                                                          //
// Very good video on querying FireStore specifically:                                      //
// https://www.youtube.com/watch?time_continue=2&v=Ofux_4c94FI&feature=emb_logo             //
//                                                                                          //
// FireStore Structure                                                                      //
// Collection: "Events"                                                                     //
// Documents: "[Date of Entered Event]" (e.g. 02032020 for and Event on Feb 3, 2020)        //
//                                                                                          //
// ** Firestore Document query still needs to be fixed. Currently ALL events from a day     //
//                                                                                          //
//                                                                                          //
//------------------------------------------------------------------------------------------//

package com.example.campusconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class View_Calendar extends AppCompatActivity {
    
    CalendarView calendar;
    TextView eventDate;
    TextView eventOrganization;
    TextView eventDescription;
    boolean newDaySelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_view_calendar);
        calendar = findViewById(R.id.calendarView);
        
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                /*eventDate = findViewById(R.id.Date);
                eventOrganization = findViewById(R.id.Organization);
                eventDescription = findViewById(R.id.Description);*/
    
                EventView eventView;
                StringBuilder dayMonthYear = new StringBuilder();
    
                if ((month + 1) < 10){
                    dayMonthYear.append('0');
                    dayMonthYear.append(month + 1);
                }
                else
                    dayMonthYear.append(month + 1);
    
                if (day < 10) {
                    dayMonthYear.append('0');
                    dayMonthYear.append(day);
                }
                else
                    dayMonthYear.append(day);

                dayMonthYear.append(year);
    
                // EventView constructor will most likely replace the old displayEvents method
                //eventView = new EventView(year, month, day);
                //displayEvents(dayMonthYear.toString());
            }
        });
        
        //clearTextViews();
        
    }// end [ METHOD: onCreate ]
    
}//end [ CLASS: View_Calendar ]