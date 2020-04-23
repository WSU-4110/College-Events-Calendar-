package com.example.campusconnect;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.campusconnect.Event.EventView;

import org.junit.Before;
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
public class MainActivityTest {
	static CalendarView calendar;
	
	@Test
	public void initializeBefore_newCalendarViewTestObject(){
		assertNotNull(calendar.findViewById(R.id.calendarView));
	}
	
	@Test
	public void test_onCreate_valid(){
		assertNotNull(calendar);
	}
	
//	@Test
//	public void test_openEventView_intentBuilt(){
//		Intent actual = new Intent(this, EventView.class);
//		assertNotNull(actual);
//	}
}
