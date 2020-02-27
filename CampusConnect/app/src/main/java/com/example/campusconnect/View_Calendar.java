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
        //daySelectionText = findViewById(R.id.daySelection);
        
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                eventDate = findViewById(R.id.Date);
                eventOrganization = findViewById(R.id.Organization);
                eventDescription = findViewById(R.id.Description);
    
                StringBuilder dayMonthYear = new StringBuilder();
                
                // TODO: wut
                // TODO: The day "12/7/2020" is being output after the below .append()'s were added
    
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
                
                displayEvents(dayMonthYear.toString());
            }
        });
        
        //clearTextViews();
        
    }// end [ METHOD: onCreate ]
    
    private void displayEvents(String dateSelected) {
        // Current Tasks (Jay and Ponnila):
        // 1. Have .whereEqualTo() set for Collections, Documents, or Fields
        // 2. How to query data with a non-literal String
        //              i.e. Date from calendar selected by user that is passed to this method
        // 3. Clear the TextView objects (Strings are stacking between day changes)
        // 4a. Make functional a boolean variable for scenario where user clicks on a day that
        //          was previously chosen (optimize/minimize number of DB queries)
        // 4b. Determine how to handle scenario outlined in 4a (be it local storage event data, etc)
        
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        
        // Collection: JayTesting
        // date in .whereEqualTo is the "date" field within a document (not the document name)
        db.collection("JayTesting")
                .whereEqualTo("date", dateSelected)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Event event = document.toObject(Event.class);
                                //event = document.toObject(Event.class);
                                
                                eventDate.setText(event.getMonth_Name());
                                eventDate.append(" ");
                                eventDate.append(event.getDay());
                                
                                eventOrganization.setText("Organization: ");
                                eventOrganization.append(event.getOrg());
    
                                eventDescription.setText("Event Description: ");
                                eventDescription.append(event.getDesc());
                            }
                        }
                    }
                });
    
        //clearText();
        
    }// end [ METHOD: displayEvents ]*/
    
    private void clearText(){
        eventDate.setText("");
        eventOrganization.setText("");
        eventDescription.setText("");
        
        System.out.println("CLEAR CALLED");
    }
    
}//end [ CLASS: View_Calendar ]