package com.example.campusconnect.Event;

import android.widget.ListView;
import android.widget.TextView;

import com.example.campusconnect.R;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class EventViewTest {
    private static TextView title;
    private static ListView testListView;
    
    // Note: Month indices begin at 0 for January (i.e. April == 3)
    
    @Test
    public void titleCreator_Full() {
        EventView valTitle = new EventView();
        assertEquals("April 15, 2020", new EventView().titleCreator("15","03","2020"));
    }

    @Test
    public void wholeDateBuilder_Full() {
        EventView valDateBuilder = new EventView();
        assertEquals("4/15/2020", new EventView().wholeDateBuilder("15","03","2020"));
    }
    
    @Test
    public void wholeDateBuilder_monthParsedCorrectly(){
        
        String testMonth = "2";
        int monthNumber;
        
        monthNumber = Integer.parseInt(testMonth);
        assertEquals(2, monthNumber);
        
        monthNumber = Integer.parseInt(testMonth) + 1;
        assertEquals(3, monthNumber);
    }
    
    @Test
    public void test_titleCreator_blankInfoDoesNotCrashApp() {
        assertNotNull(new EventView().wholeDateBuilder(" "," "," "));
    }
    
    @Test
    public void test_wholeDateInfo_blankInfoDoesNotCrashApp() {
        assertNotNull(new EventView().titleCreator(" "," "," "));
    }
    
    // "display()" == "displayEventsForSelectedDay()"
    @Test
    public void display_listViewInitializes(){
        testListView = testListView.findViewById(R.id.events_listView);
        assertNotNull(testListView);
    }
    
    
}