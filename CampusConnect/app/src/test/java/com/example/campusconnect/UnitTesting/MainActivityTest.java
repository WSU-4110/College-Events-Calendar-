package com.example.campusconnect.UnitTesting;

import android.content.Intent;
import android.widget.CalendarView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusconnect.Event.EventView;
import com.example.campusconnect.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class MainActivityTest extends AppCompatActivity {
	static CalendarView calendar;

	@BeforeAll
	public void initializeBefore_newCalendarViewTestObject(){
		assertNotNull(calendar.findViewById(R.id.calendarView));
	}

	@Before
	public void test_onCreate_valid(){
		assertNotNull(this);
	}

	@Test
	public void test_openEventView_intentBuilt(){
		Intent actual = new Intent(this, EventView.class);
		assertNotNull(actual);
	}

	// Espresso needed for click
}