package com.example.campusconnect.Event;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.campusconnect.MainActivity;
import com.example.campusconnect.R;
import com.example.campusconnect.UI.Authentication.signIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Objects;


class Event {

    private String name;
    private String location;
    private String startTime;
    private String date;
    private String org;
    private String desc;
    private String uid;
    private String OrgUid;

    
    Event() {
        name = "EventNameNotProvided";
        location = "NoEventLocationProvided";
        startTime = "11:59";
        date = "01012019";
        org = "EventOrgNotEntered";
        desc = "N/A";
    }
    
    Event(String dateSelected){
        this.date = dateSelected;
    }
    
    public Event(String name, String location, String startTime, String date, String org, String desc) {
        this.name = name;
        this.location = location;
        this.startTime = startTime;
        this.date = date;
        this.org = org;
        this.desc = desc;
    }
    public Event(String uid, String name, String location, String startTime, String date, String org, String desc, String OrgUid) {
        this.uid = uid;
        this.name = name;
        this.location = location;
        this.startTime = startTime;
        this.date = date;
        this.org = org;
        this.desc = desc;
        this.OrgUid = OrgUid;
    }

    public String getName() { return name; }
    
    public String getLocation() { return location; }
    
    public String getStartTime() { return startTime; }

    public String getDate() { return date; }

    public String getOrg() { return org; }

    public String getDesc() { return desc; }

    public String getUid() { return uid; }

    public String getOrgUid() { return OrgUid; }

    public void setName(String name) { this.name = name; }

    public void setLocation(String location) { this.location = location; }

    public void setStartTime(String startTime) { this.startTime = startTime; }

    public void setDate(String date) { this.date = date; }

    public void setOrg(String org) { this.org = org; }

    public void setDesc(String desc) { this.desc = desc; }

    public void setUid(String uid) { this.uid = uid; }

    public void setOrgUid(String Orguid) { this.OrgUid = OrgUid; }

    public String toString()
	{
        return name + "|" + location + "|" + startTime + "|" + date + "|" + org + "|" + desc + "|" + OrgUid;
    }



}// end [ CLASS: Event ]



public class EventCreation extends AppCompatActivity{

    String EventName, location, startTime, date, desc, org;

    private static final String TAG = "Event";

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    EditText EventNameInput;
    EditText locationInput;
    EditText startTimeInput;
    EditText dateInput;
    EditText descInput;
    EditText orgInput;

    Button submitButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static boolean Organizer = false; //adding organizer code

    public static boolean isOrganizer() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //Organizer = false;
        if (user!=null)
        {
            db.collection("Users")
                    .document("Organizers")
                    .collection("FirebaseID")
                    .whereEqualTo("id", user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//
//                                //Organizer = !(task.getResult().isEmpty());
//                                Organizer = true;
//                                //adapter.addAll(arrayOfEvents);
//                            }
                            if (!task.getResult().isEmpty()){
                                Organizer = true;

                            }
                        }
                    }
                    );
            if (Organizer) {
                Organizer = false;
                return true;
            }
            else{
                //Organizer = false;
                return false;
            }

        }
        else{
            //Organizer = false;
            return false;
        }

    }


    // <Event>.add() auto-generates a unique ID. Side Effect: "Submit Event" can make duplicates
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_creation);

        mDisplayDate = (TextView) findViewById(R.id.EventDate);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        EventCreation.this,
                        android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Log.d(TAG, "onDateSet: date: " + month + "/" + dayOfMonth + "/" + year);
                month = month +1;
                String date = month + "/" + dayOfMonth + "/" + year;
                mDisplayDate.setText(date);
            }
        };

        submitButton = (Button)findViewById(R.id.event_submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventNameInput = (EditText) findViewById(R.id.EventName);
                locationInput = (EditText) findViewById(R.id.Location);
                startTimeInput = (EditText) findViewById(R.id.Time);
                dateInput = (EditText) findViewById(R.id.EventDate);
                descInput = (EditText) findViewById(R.id.Description);
                orgInput = (EditText) findViewById(R.id.Organization);

                EventName = EventNameInput.getText().toString();
                location = locationInput.getText().toString();
                startTime = startTimeInput.getText().toString();
                date = dateInput.getText().toString();
                desc = descInput.getText().toString();
                org = orgInput.getText().toString();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Event event = new Event(null, EventName, location, startTime,
                        date, org, desc, user.getUid());

                db.collection("Events")
                        .document("Events")
                        .collection("Event_SubCollectionTesting")
                        .add(event);

                Toast.makeText(EventCreation.this, "Event Added", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EventCreation.this, MainActivity.class);
                startActivity(intent);
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Event Creation");
    }

    @Override public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.newEvent){

        }

        if(item.getItemId()==R.id.login)
        {

                Intent intent = new Intent(this, signIn.class);
                startActivity(intent);

        }
        else if(item.getItemId()==R.id.logout){
            final FirebaseAuth mAuth=FirebaseAuth.getInstance();
            startActivity(new Intent(EventCreation.this, signIn.class));
            //FirebaseAuth.getInstance().signOut();
            mAuth.signOut();
        }
        else
        {
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }
    
}// end [ CLASS: EventCreation ]






