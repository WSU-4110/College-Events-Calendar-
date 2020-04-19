package com.example.campusconnect.UnitTesting;

import junit.framework.TestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({ MainActivityTest.class, EventListAdapterTest.class, EventViewTest.class })
public class AllTests {

	Class[] testClasses = { MainActivityTest.class, EventListAdapterTest.class, EventViewTest.class };
	TestSuite suite = new TestSuite(testClasses);

}
