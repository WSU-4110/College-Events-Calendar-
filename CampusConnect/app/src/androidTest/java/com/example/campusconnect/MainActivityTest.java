package com.example.campusconnect;

import android.content.Intent;
import android.widget.CalendarView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.campusconnect.Event.EventView;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends AppCompatActivity {
	private static CalendarView calendar;
	
	@Test
	public void test_onCreate_ClearAndValid() {
		assertNotNull(calendar);
	}
	
	@Test
	public void test_calendarViewLocatesIDCorrect() {
		calendar = calendar.findViewById(R.id.calendarView);
		assertNotNull(calendar);
	}
	
	@Test
	public void test_openEventView_intentBuilt() {
		Intent actualIntent = new Intent(this, EventView.class);
		assertNotNull(actualIntent);
	}
}
