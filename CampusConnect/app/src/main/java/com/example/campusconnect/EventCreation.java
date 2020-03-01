package com.example.campusconnect;


import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


class Event {


    private String name;
    private String location;
    private String startTime;
    private String date;
    private String org;
    private String desc;
    
    Event() {
        name = "EventNameNotProvided";
        location = "NoEventLocationProvided";
        startTime = "11:59";
        date = "01012019";
        org = "EventOrgNotEntered";
        desc = "N/A";
    }
    
    
    public Event(String name, String location, String startTime, String date, String org, String desc) {
        this.name = name;
        this.location = location;
        this.startTime = startTime;
        this.date = date;
        this.org = org;
        this.desc = desc;
    }

    public String getName() { return name; }
    
    public String getLocation() { return location; }
    
    public String getStartTime() { return startTime; }

    public String getDate() { return date; }

    public String getOrg() { return org; }

    public String getDesc() { return desc; }

    public void setName(String name) { this.name = name; }

    public void setLocation(String location) { this.location = location; }

    public void setStartTime(String startTime) { this.startTime = startTime; }

    public void setDate(String date) { this.date = date; }

    public void setOrg(String org) { this.org = org; }

    public void setDesc(String desc) { this.desc = desc; }
    
    
    // !! TODO: Change date/time storage to Calendar
    
    // CHECK: Store time 24h?
    public String getStartTime_Formatted(){
        switch(this.startTime.length())
        {
            case 3:
                // _H:MM
                return String.format(" %c:%c%c",
                        startTime.charAt(0),
                        startTime.charAt(1),
                        startTime.charAt(2));
            case 4:
                // HH:MM
                return String.format("%c%c:%c%c",
                        startTime.charAt(0),
                        startTime.charAt(1),
                        startTime.charAt(2),
                        startTime.charAt(3));
            default:
                // STORED-TIME FORMAT ERROR
                return "00:00";
        }
    }
    
    // String Formatting: Java Textbook Chapter 10.10.7
    public String getDate_Formatted(){
        return String.format("%s/%s/%s", getMonth_Integer(), getDay(), getYear());      // MM/DD/YYYY
    }
    
    private String getMonth_Integer() {
        return new String(new char[]{date.charAt(0), date.charAt(1)});
    }
    
    private String getDay() {
        //StringBuilder dayBuilder = new StringBuilder();
        String dayBuilder = new String(new char[]{date.charAt(2), date.charAt(3)});
        
        return dayBuilder + dayModifier();
    }
    
    private String dayModifier()
    // Character at index String[3] determines the contraction
    // e.g. 02052020: String Index[3] == 5 -> Contraction: 'rd' -> Feb 3[rd] 2020
    {
        char dayDigit = this.date.charAt(3);
        
        if(dayDigit == '1')
            return "st";
        else if (dayDigit == '2')
            return "nd";
        else if (dayDigit == '3')
            return "rd";
        else
            return "th";
    }
    
    
    private String getYear() {
        return new String(new char[]{date.charAt(4), date.charAt(5),
                date.charAt(6), date.charAt(7),});
    }
    
    // Int Parser: Java Textbook Chapter 10.10.6
    private String getMonth_Name() {
        int month;
        String monthName;
        
        month = Integer.parseInt(new String(new char[]{date.charAt(0), date.charAt(1)}));
        month++;
        
        switch(month){
            case 1:
                monthName = "January";
                break;
            case 2:
                monthName = "February";
                break;
            case 3:
                monthName = "March";
                break;
            case 4:
                monthName = "April";
                break;
            case 5:
                monthName = "May";
                break;
            case 6:
                monthName = "June";
                break;
            case 7:
                monthName = "July";
                break;
            case 8:
                monthName = "August";
                break;
            case 9:
                monthName = "September";
                break;
            case 10:
                monthName = "October";
                break;
            case 11:
                monthName = "November";
                break;
            case 12:
                monthName = "December";
                break;
            default:
                monthName = "Error";
                break;
        }
        
        return monthName;
    }
  
}// end [ CLASS: Event ]



public class EventCreation extends AppCompatActivity{

    String EventName, location, startTime, date;

    EditText EventNameInput;
    EditText locationInput;
    EditText startTimeInput;
    EditText dateInput;

    Button submitButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    

    // TODO: .add() auto-generates a unique ID,
    //  but this also means you can keep clicking "Submit Event" and it'll make duplicates
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_creation);
        
        submitButton = findViewById(R.id.event_submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("START onCreate");
                EventNameInput = (EditText) findViewById(R.id.EventName);
                locationInput = (EditText) findViewById(R.id.Location);
                startTimeInput = (EditText) findViewById(R.id.Time);
                dateInput = (EditText) findViewById(R.id.EventDate);

                EventName = EventNameInput.getText().toString();
                location = locationInput.getText().toString();
                startTime = startTimeInput.getText().toString();
                date = dateInput.getText().toString();
                Event event = new Event(EventName, location, startTime,
                        date, "Org", "Desc");
                //db.collection("Events").document("Events").set(event); // Original
                
                // Using .add() makes FireStore auto generate a random ID (as opposed to .set())
                db.collection("JayTesting").add(event);
            }
        });
    }
    
}// end [ CLASS: EventCreation ]











