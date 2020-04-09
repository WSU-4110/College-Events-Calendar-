package com.example.campusconnect.Event;

// Documentation on ListView and Custom Adapters:
// https://bit.ly/38ah7WV

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusconnect.MainActivity;
import com.example.campusconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;


public class SavedEvent extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_view);
        listView = (ListView) findViewById(R.id.events_listView);



        displayEventsForSelectedUser();

    }


    // TODO: Look into feasibility of adding Left and Right arrows once event list is open
    private void displayEventsForSelectedUser() {

        TextView title = findViewById(R.id.EventList_HeaderDynamic);
        title.setText("Your Saved Events");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<Event> arrayOfEvents;
        final SavedEventListAdapter adapter;

        // TODO: Discuss a more intuitive name than "wholeDate"

        // [A]
        arrayOfEvents = new ArrayList<>();                                      // [1]
        adapter = new SavedEventListAdapter(this, arrayOfEvents);		// [2]
        listView = (ListView) findViewById(R.id.events_listView);               // [3]
        listView.setAdapter(adapter);											// [4]

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0,View arg1, int position, long arg3)
            {
                Event event = (Event)listView.getAdapter().getItem(position);
                Intent intent = new Intent(getApplicationContext(), EventDetailedView.class);
                intent.putExtra("Event", event.toString());
                startActivity(intent);
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



        if (user!=null){
            final boolean[] Organizer = {false}; //adding organizer code
            db.collection("User")
                    .document("Organizers")
                    .collection("FirebaseID")
                    .whereEqualTo("id", user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Organizer[0] = true;
                            }
                        }
                    });
            if (!Organizer[0]){

                db.collection("SavedEvent")
                        .document("SavedEvent")
                        .collection("Event_SubCollectionTesting")
                        .whereEqualTo("uid", user.getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                        //arrayOfEvents.add(document.toObject(Event.class));
                                        adapter.add((Event) document.toObject(Event.class));
                                    }

                                    //adapter.addAll(arrayOfEvents);
                                }
                            }
                        });

            }
            if (Organizer[0]){

                db.collection("SavedEvent")
                        .document("SavedEvent")
                        .collection("Event_SubCollectionTesting")
                        .whereEqualTo("Orguid", user.getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                        //arrayOfEvents.add(document.toObject(Event.class));
                                        adapter.add((Event) document.toObject(Event.class));
                                    }

                                    //adapter.addAll(arrayOfEvents);
                                }
                            }
                        });

            }
        }



        else{
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }


    }// END METHOD [ displayEventsForSelectedDay ]




}// END CLASS [ EventView ]


class SavedEventListAdapter extends ArrayAdapter<Event>  {

    public SavedEventListAdapter(Context context, ArrayList<Event> events){
        super(context, 0, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        // [B]
        if (convertView == null) {                                                                      // [1]
            convertView = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.list_events, parent, false);
        }

        Event event = getItem(position);                                                                // [2]

        TextView eventName =        (TextView) convertView.findViewById(R.id.list_EventName);           // [3a]
        TextView eventDate =        (TextView) convertView.findViewById(R.id.list_EventDate);			// [3b]
        TextView eventLocation =    (TextView) convertView.findViewById(R.id.list_EventLocation);		// [3b]

        eventName.setText("Event Name:    ");                                                           // [4]
        eventName.append(event.getName());

        eventDate.setText("Date:    ");
        eventDate.append(event.getDate());

        eventLocation.setText("Location:    ");
        eventLocation.append(event.getLocation());

        return convertView;                                                                             // [5]

    }

}// END CLASS [ EventListAdapter ]




//--------------------------------------------------- NOTES ---------------------------------------------------//

//				[A]
// --| ATTACHING THE ADAPTER TO A LIST VIEW |--
// 1. Construct the data source
// 2. Create the Adapter to convert the array to views
// 3. Declare the ListView object
// 4. Attach the adapter to a ListView (XML)

// 				[B]
// --| CUSTOM ADAPTER CLASS [B] |--
// 1. Check if an existing view is being reused, otherwise inflate the view
// 2. Get the data item for this position
// 3. Lookup view for data population
// 4. Populate template view using the temp Event object
// 5. Return the completed view to render on screen



//package com.example.campusconnect;
//
//public class SavedEvent {
//
//        private String userId;
//        private String name;
//        private String location;
//        private String startTime;
//        private String date;
//        private String org;
//        private String desc;
//
//
//    SavedEvent() {
//            userId = "UserIdNotProvided";
//            name = "EventNameNotProvided";
//            location = "NoEventLocationProvided";
//            startTime = "11:59";
//            date = "01012019";
//            org = "EventOrgNotEntered";
//            desc = "N/A";
//        }
//
//    SavedEvent(String dateSelected){
//            this.date = dateSelected;
//        }
//
//        public SavedEvent(String userId, String name, String location, String startTime, String date, String org, String desc) {
//            this.userId = userId;
//            this.location = location;
//            this.startTime = startTime;
//            this.date = date;
//            this.org = org;
//            this.desc = desc;
//        }
//
//        public String getUserId() { return userId; }
//
//        public String getName() { return name; }
//
//        public String getLocation() { return location; }
//
//        public String getStartTime() { return startTime; }
//
//        public String getDate() { return date; }
//
//        public String getOrg() { return org; }
//
//        public String getDesc() { return desc; }
//
//        public void setUserId(String userId) { this.userId = userId; }
//
//        public void setName(String name) { this.name = name; }
//
//        public void setLocation(String location) { this.location = location; }
//
//        public void setStartTime(String startTime) { this.startTime = startTime; }
//
//        public void setDate(String date) { this.date = date; }
//
//        public void setOrg(String org) { this.org = org; }
//
//        public void setDesc(String desc) { this.desc = desc; }
//
//        public String toString()
//        {
//            return userId + "|" + name + "|" + location + "|" + startTime + "|" + date + "|" + org + "|" + desc;
//        }
//        // UPDATE: to date/time storage to java.util.Calendar
//
//        public String getStartTime_Formatted(){
//            switch(this.startTime.length())
//            {
//                case 3:
//                    // _H:MM
//                    return String.format(" %c:%c%c",
//                            startTime.charAt(0),
//                            startTime.charAt(1),
//                            startTime.charAt(2));
//                case 4:
//                    // HH:MM
//                    return String.format("%c%c:%c%c",
//                            startTime.charAt(0),
//                            startTime.charAt(1),
//                            startTime.charAt(2),
//                            startTime.charAt(3));
//                default:
//                    // STORED-TIME FORMAT ERROR
//                    return "00:00";
//            }
//        }
//
//        // OUTPUT FORMAT: MM/DD/YYYY
//        public String getDate_Standard(){
//            return String.format("%s/%s/%s", getMonth_Integer(), getDay(), getYear());
//        }
//
//        private String getDay() {
//            return new String(new char[]{date.charAt(2), date.charAt(3)});
//        }
//
//        private String getMonth_Integer() {
//            return new String(new char[]{date.charAt(0), date.charAt(1)});
//        }
//
//        // OUTPUT FORMAT: Month (D)th, YYYY
//        public String getDate_Full(){
//            return String.format("%s %s, %s", getMonth_Name(), getDay_wContraction(), getYear());
//        }
//
//        private String getDay_wContraction() {
//            String dayBuilder = new String(new char[]{date.charAt(2), date.charAt(3)});
//
//            return dayBuilder + dayModifier();
//        }
//
//        // Character at index String[3] determines the contraction
//        // e.g. 02052020: String Index[3] == 5 -> Contraction: 'rd' -> Feb 3[rd] 2020
//        private String dayModifier()
//        {
//            char dayDigit = this.date.charAt(3);
//
//            if(dayDigit == '1')
//                return "st";
//            else if (dayDigit == '2')
//                return "nd";
//            else if (dayDigit == '3')
//                return "rd";
//            else
//                return "th";
//        }
//
//        private String getYear() {
//            return new String(new char[]{date.charAt(4), date.charAt(5),
//                    date.charAt(6), date.charAt(7),});
//        }
//
//        // Int Parser: Java Textbook Chapter 10.10.6
//        private String getMonth_Name() {
//            int month;
//            String monthName;
//
//            month = Integer.parseInt(new String(new char[]{date.charAt(0), date.charAt(1)}));
//            //month++;
//
//            switch(month){
//                case 1:
//                    monthName = "January";
//                    break;
//                case 2:
//                    monthName = "February";
//                    break;
//                case 3:
//                    monthName = "March";
//                    break;
//                case 4:
//                    monthName = "April";
//                    break;
//                case 5:
//                    monthName = "May";
//                    break;
//                case 6:
//                    monthName = "June";
//                    break;
//                case 7:
//                    monthName = "July";
//                    break;
//                case 8:
//                    monthName = "August";
//                    break;
//                case 9:
//                    monthName = "September";
//                    break;
//                case 10:
//                    monthName = "October";
//                    break;
//                case 11:
//                    monthName = "November";
//                    break;
//                case 12:
//                    monthName = "December";
//                    break;
//                default:
//                    monthName = "Error";
//                    break;
//            }
//
//            return monthName;
//        }
//
//
//}
