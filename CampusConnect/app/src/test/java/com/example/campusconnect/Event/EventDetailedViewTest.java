package com.example.campusconnect.Event;

import com.example.campusconnect.Event.EventDetailedView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.junit.Test;

import static org.junit.Assert.*;

public class EventDetailedViewTest {

    @Test
    public void checkLocationValid() {
        EventDetailedView valLocation = new EventDetailedView();
        assertEquals(true, new EventDetailedViewTest().checkLocationValid("Student Center"));
        assertEquals(false, new EventDetailedViewTest().checkLocationValid(null));
    }

    @Test
    public void checkNameEmpty() {
        EventDetailedView valName = new EventDetailedView();
        assertEquals(true, new EventDetailedViewTest().checkNameEmpty("Orientation"));
        assertEquals(false, new EventDetailedViewTest().checkNameEmpty(null));
    }

    @Test
    public void validateStartTime() {
        EventDetailedView valStartTime = new EventDetailedView();
        assertEquals(false, new EventDetailedViewTest().validateStartTime("s"));
        assertEquals(true, new EventDetailedViewTest().validateStartTime("5"));
    }

    @Test
    public void validateDate() {
        EventDetailedView valDate = new EventDetailedView();

        assertEquals(true, new EventDetailedViewTest().validateDate("03/03/2020"));
    }
    
    // vadation methods
    public boolean checkLocationValid(String location) {
        if (location == null || location.isEmpty()) {
            System.out.println("Name  is empty.");
            return false;
        }
        
        return true;
    }
    
    public boolean checkNameEmpty(String name) {
        
        if (name == null || name.isEmpty()) {
            System.out.println("Name  is empty.");
            return false;
        }
        
        return true;
    }
    
    public boolean validateStartTime(String startTime) {
        try {
            int startT = Integer.parseInt(startTime);
            System.out.println(startT);
        }
        catch (NumberFormatException nfe) {
            return false;
        }
        
        return true;
    }
    
    public boolean validateDate(String date) {
        DateFormat sdf = new SimpleDateFormat(date);
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            System.out.println(" ~~~~~~~~ " + sdf.parse(date));
        }
        catch (ParseException e) {
            return false;
        }
        
        return true;
    }
    
}