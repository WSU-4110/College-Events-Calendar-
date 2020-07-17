package com.example.campusconnect.Event;

// Documentation on ListView and Custom Adapters:
// https://bit.ly/38ah7WV

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.campusconnect.R;

import java.util.ArrayList;


// Package-Private Class
class EventListAdapter extends ArrayAdapter<Event> {
	
	public EventListAdapter(Context context, ArrayList<Event> events) {
		super(context, 0, events);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {                                                    	// [1]
			convertView = LayoutInflater
					.from(getContext())
					.inflate(R.layout.events_list_item, parent, false);
		}
		
		Event event = getItem(position);                                            	// [2]
		
		TextView eventName = convertView.findViewById(R.id.list_EventName);        		// [3a]
		TextView eventDate = convertView.findViewById(R.id.list_EventDate);        		// [3b]
		TextView eventLocation = convertView.findViewById(R.id.list_EventLocation);		// [3c]
		TextView eventTag = convertView.findViewById(R.id.list_EventTag);        		// [3d]
																						// [4]
		eventName.setText(event.getName());
		
		eventDate.setText("Date:    ");
		eventDate.append(event.getDate());
		
		eventLocation.setText("Location:    ");
		eventLocation.append(event.getLocation());
		
		eventTag.setText("Tag:    ");
		eventTag.append(event.tag());
		
		return convertView;                                                            // [5]
	}
	
}// class [ EventListAdapter ]


//--------------------------------------------------- NOTES ---------------------------------------------------//
// --| CUSTOM ADAPTER CLASS |--
// 1. Check if an existing view is being reused, otherwise inflate the view
// 2. Get the data item for this position
// 3. Lookup view for data population
// 4. Populate template view using the temp Event object
// 5. Return the completed view to render on screen