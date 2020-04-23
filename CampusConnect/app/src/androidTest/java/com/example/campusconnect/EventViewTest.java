package com.example.campusconnect;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class EventViewTest {
	static ListView testListView;
	static final String testDay = "14";
	static final String testMonth = "3";		// !! NOTE: Jan == 0
	static final String testYear = "2020";
	
	// "display()" == "displayEventsForSelectedDay()"
	@Test
	public void display_listViewInitializes(){
		testListView = testListView.findViewById(R.id.events_listView);
		assertNotNull(testListView);
	}
	
	@Test
	public void wholeDateBuilder_monthParsedCorrectly(){
		
		int monthNumber;
		
		monthNumber = Integer.parseInt(testMonth);
		assertEquals(3, monthNumber);
		
		monthNumber = Integer.parseInt(testMonth) + 1;
		assertEquals(4, monthNumber);
	}
	
	@Test
	public void wholeDateBuilder_buildsFormattedDateCorrectly(){
		
		StringBuilder date = new StringBuilder();
		
		int monthNumber = Integer.parseInt(testMonth) + 1;
		
		date.append(monthNumber);
		date.append("/");
		
		date.append(testDay);
		date.append("/");
		date.append(testMonth);
		
		//date.toString();			// Original
		assertEquals("4/13/2020", date.toString());
	}
}
