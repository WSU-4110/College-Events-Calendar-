package com.example.campusconnect.Event;


import org.junit.Test;


import static org.junit.Assert.*;


public class SearchTest {



    @Test
    public void returnProperString() {
        String string="testing";
        assertEquals("Testing", Search.returnProperString(string));
    }


    @Test
    public void returnExtraString(){
        String test= "test";
        String expected= "test ";
        assertEquals(expected, Search.returnExtraString(test));
    }


}