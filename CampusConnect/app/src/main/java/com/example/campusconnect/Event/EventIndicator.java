package com.example.campusconnect.Event;

import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.github.sundeepk.compactcalendarview.domain.Event;

import static com.github.sundeepk.compactcalendarview.domain.Event.*;
/*
<color name="primaryGreen_50">#8000594C</color>
<color name="primaryGreen_75">#BF00594C</color>
<color name="primaryGreen_100">#00594C</color>
<color name="primaryGold_50">#BFFFCC33</color>
<color name="primaryGold_75">#BFFFCC33</color>
<color name="primaryGold_100">#FFCC33</color>
<color name="Red">#D81B60</color>
<color name="White">#FFFFFF</color>
<color name="Wheat">#F5DEB3</color>
<color name="Navy">#000080</color>
*/


public class EventIndicator extends Event {
	
	private long timeInMillis;
	private Object data;
	private String colorHex = "FFCC33";
	private Calendar calendarDAO;
	
	public EventIndicator(EventIndicator event) {
		super(Color.parseColor(event.colorHex), event.timeInMillis);
		this.timeInMillis = event.timeInMillis;
		
		calendarDAO = Calendar.getInstance();
		calendarDAO.setTimeInMillis(event.timeInMillis);
	}
	
	public EventIndicator(long timeInMillis) {
//		super(Color.parseColor("FFCC33"), timeInMillis);
		super(Color.BLUE, timeInMillis);
		this.timeInMillis = timeInMillis;
		calendarDAO = Calendar.getInstance();
		calendarDAO.setTimeInMillis(timeInMillis);
	}
	
	public EventIndicator(int color, long timeInMillis) {
		super(color, timeInMillis);
		this.timeInMillis = timeInMillis;
	}
	
	public EventIndicator(Date date) {
		super(Color.parseColor("FFCC33"), date.getTime());
		this.timeInMillis = date.getTime();
	}
	
	public EventIndicator(String colorHex, long timeInMillis) {
		super(Color.parseColor(colorHex), timeInMillis);
		this.colorHex = colorHex;
		this.timeInMillis = timeInMillis;
	}
	
	public int getDay(){
		Calendar cal = Calendar.getInstance();
		
		cal.setTimeInMillis(this.timeInMillis);
		return cal.get(Calendar.DAY_OF_WEEK);
	}
	
	public int getMonth(){
		Calendar cal = Calendar.getInstance();
		
		cal.setTimeInMillis(this.timeInMillis);
		return cal.get(Calendar.MONTH);
	}
	
	public int getYear(){
		Calendar cal = Calendar.getInstance();
		
		cal.setTimeInMillis(this.timeInMillis);
		return cal.get(Calendar.YEAR);
	}
	
	
}// [ EventIndicator ]

/**
 * Sets this Calendar's time with the given <code>Date</code>.
 * <p>
 * Note: Calling <code>setTime()</code> with
 * <code>Date(Long.MAX_VALUE)</code> or <code>Date(Long.MIN_VALUE)</code>
 * may yield incorrect field values from <code>get()</code>.
 *
 * @param date the given Date.
 * @see #getTime()
 * @see #setTimeInMillis(long)
 */
//	public final void setTime(Date date) {
//		setTimeInMillis(date.getTime());
//	}