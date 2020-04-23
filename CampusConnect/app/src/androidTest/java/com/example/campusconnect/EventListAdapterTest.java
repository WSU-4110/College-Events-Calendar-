package com.example.campusconnect;

import android.view.View;
import android.view.ViewGroup;
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
public class EventListAdapterTest {
	static View convertView;
	static ViewGroup parent;
	static TextView title;
	
	// Event info matches actual event in DB
	static final String testTitle = "Freshmen Orientation";
	static final String testDate = "4/14/2020";
	static final String testLocation = "Library";
	
	@Test
	public void test_titleCreator_notNullTextView() {
		title = title.findViewById(R.id.EventList_HeaderDynamic);
		assertNotNull(title);
	}
	
	@Test
	public void test_titleCreator_createsCorrectTitle() {
		title = title.findViewById(R.id.EventList_HeaderDynamic);
		assertEquals("April 14, 2020", title.getText().toString());
	}
}
