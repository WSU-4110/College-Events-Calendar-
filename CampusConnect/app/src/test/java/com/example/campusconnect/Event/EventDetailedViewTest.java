package com.example.campusconnect.Event;

import com.example.campusconnect.Event.EventDetailedView;

import org.junit.Test;

import static org.junit.Assert.*;

public class EventDetailedViewTest {

    @Test
    public void checkLocationValid() {
        EventDetailedView valLocation = new EventDetailedView();
        assertEquals(true, new EventDetailedView().checkLocationValid("Student Center"));
        assertEquals(false, new EventDetailedView().checkLocationValid(null));
    }

    @Test
    public void checkNameEmpty() {
        EventDetailedView valName = new EventDetailedView();
        assertEquals(true, new EventDetailedView().checkNameEmpty("Orientation"));
        assertEquals(false, new EventDetailedView().checkNameEmpty(null));
    }

    @Test
    public void validateStartTime() {
        EventDetailedView valStartTime = new EventDetailedView();
        assertEquals(false, new EventDetailedView().validateStartTime("s"));
        assertEquals(true, new EventDetailedView().validateStartTime("5"));
    }

    @Test
    public void validateDate() {
        EventDetailedView valDate = new EventDetailedView();

        assertEquals(true, new EventDetailedView().validateDate("03/03/2020"));
    }
}