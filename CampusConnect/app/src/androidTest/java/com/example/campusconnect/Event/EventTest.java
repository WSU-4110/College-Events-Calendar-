package com.example.campusconnect.Event;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Test;

import static org.junit.Assert.*;

public class EventTest {



    public static boolean Organizer;

    @Test
    public void testIsOrganizer() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Organizer = false;
                    db.collection("Users")
                    .document("Organizers")
                    .collection("FirebaseID")
                    .whereEqualTo("id", "nouser")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                               @Override
                                               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                   if (!task.getResult().isEmpty()){
                                                       Organizer = true;

                                                   }
                                                   assertEquals(false,Organizer);
                                               }
                                           }
                    );


        Organizer = false;

        db.collection("Users")
                .document("Organizers")
                .collection("FirebaseID")
                .whereEqualTo("id", "0BD4Ne15vdXtxdFWgElFOny6YBA3")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                               if (!task.getResult().isEmpty()){
                                                   Organizer = true;
                                               }
                                               assertEquals(true,Organizer);
                                           }
                                       }
                );






        }


    @Test
    public void testConstructor(){
        Event event1 = new Event("5","John","detroit","6:00",
                "12/12/2012","The Devs", "fun","12345","Sports");
        assertEquals("5", event1.getUID());
        assertEquals("John", event1.getName());
        assertEquals("detroit", event1.getLocation());
        assertEquals("6:00", event1.getStartTime());
        assertEquals("12/12/2012", event1.getDate());
        assertEquals("The Devs", event1.getOrg());
        assertEquals("fun", event1.getDesc());
        assertEquals("12345", event1.getOrgUid());
        assertEquals("Sports", event1.tag());

    }

    @Test
    public void testSetuid(){
        Event event1 = new Event("5","John","detroit","6:00",
                "12/12/2012","The Devs", "fun","12345","Sports");
        event1.setUid("3");
        assertEquals("3",event1.getUID());
    }

    @Test
    public void testGetUid(){
        Event event1 = new Event("5","John","detroit","6:00",
                "12/12/2012","The Devs", "fun","12345","Sports");
        String testUid = event1.getUID();
        assertEquals("5",testUid);
    }

    @Test
    public void testSetName(){
        Event event1 = new Event("5","John","detroit","6:00",
                "12/12/2012","The Devs", "fun","12345","Sports");
        event1.setName("Jose");
        assertEquals("Jose",event1.getName());
    }

    @Test
    public void testGetName(){
        Event event1 = new Event("5","John","detroit","6:00",
                "12/12/2012","The Devs", "fun","12345","Sports");
        String testName = event1.getName();
        assertEquals("John",testName);
    }

    }


