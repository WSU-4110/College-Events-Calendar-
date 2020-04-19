package com.example.campusconnect.UnitTesting;

import android.widget.ListView;

import com.example.campusconnect.R;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class EventViewTest {
	// Mockito
	// @InjectMock FirebaseFirestore db;
	// @InjectMock Task task;
	// @InjectMock QueryDocumentSnapshot document;
	// @InjectMock Event test_event;
	// @Mock EventListAdapter test_adapter;
	// @Mock ArrayList<Event> test_eventList;
	// @Mock ListView test_listView;

	static ListView testListView;
	static final String testDay = "14";
	static final String testMonth = "3";		// !! NOTE: Jan == 0
	static final String testYear = "2020";

	@BeforeAll

	// "display()" == "displayEventsForSelectedDay()"
	@Test
	public void display_listViewInitializes(){
		testListView = testListView.findViewById(R.id.events_listView);
		assertNotNull(testListView);
	}

	/*@Test
	public void display_correctDate(){
		// Espresso Needed for click
		testListViewset.OnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0,View arg1, int position, long arg3)
            {
                event = (Event)listView.getAdapter().getItem(position);		// Mock injection
                Intent intent = new Intent(getApplicationContext(), EventDetailedView.class);
                System.out.println( " Event : "+ event.toString());
                intent.putExtra("Event", event.toString());
                startActivity(intent);
	}*/

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


}// end TEST CLASS [ EventViewTest ]