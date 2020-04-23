package com.example.campusconnect.Event;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class EventViewTest {

    @Test
    public void titleCreator() {
        EventView valTitle = new EventView();
        assertEquals("April 15, 2020", new EventView().titleCreator("15","03","2020"));
    }

    @Test
    public void wholeDateBuilder() {
        EventView valDateBuilder = new EventView();
        assertEquals("4/15/2020", new EventView().wholeDateBuilder("15","03","2020"));
    }
}