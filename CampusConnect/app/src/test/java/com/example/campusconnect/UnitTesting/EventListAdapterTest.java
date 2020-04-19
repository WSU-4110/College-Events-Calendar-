package com.example.campusconnect.UnitTesting;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.campusconnect.R;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

//import com.example.campusconnect.Event.Event;
//import com.example.campusconnect.Event.EventListAdapter;

class EventListAdapterTest {

	static View convertView;
	static ViewGroup parent;
	static TextView title;

	// Event info matches actual event in DB
	static final String testTitle = "Freshmen Orientation";
	static final String testDate = "4/14/2020";
	static final String testLocation = "Library";

	// Identical copy of EventView.titleCreator()
	private String titleCreator_helper(String day, String month, String year){
		// !! NOTE: Jan == 0, Dec == 11

		int monthInteger = Integer.parseInt(month);

		String[] monthName = {	"January", "February", "March",
				"April", "May", "June",
				"July", "August", "September",
				"October", "November", "December" };

		return String.format("%s %s, %s", monthName[monthInteger], day, year);
	}

	@Test
	public void test_titleCreator_notNullTextView(){
		title = title.findViewById(R.id.EventList_HeaderDynamic);
		assertNotNull(title);
	}

	@Test
	public void test_titleCreator_createsCorrectTitle(){
		title = title.findViewById(R.id.EventList_HeaderDynamic);
		assertEquals("April 14, 2020", title.getText().toString());
	}


	/*@Test
	void getView() {
		// Espresso
		convertView = LayoutInflater
				.from(getContext())
				.inflate(R.layout.list_events, parent, false);
		assertNotNull(convertView);
	}*/

}// end TEST CLASS [ EventListAdapterTest ]
