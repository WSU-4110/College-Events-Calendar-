package com.example.campusconnect.admin;


import android.widget.FrameLayout;

import org.junit.BeforeClass;
import org.junit.Test;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class listAdapterTest {

    private static List<Map<String, Object>> emptyRequestList;
    private static List<Map<String, Object>> lightRequestList;

    private static listAdapter emptyRequestListAdapter;
    private static listAdapter lightRequestListAdapter;

    @BeforeClass
    public static void setUp(){

        //Initializing various post lists to put in our post adapter.
        List emptyPostList = new ArrayList<Map<String, Object>>();
        List lightPostList = new ArrayList<>();

        Map<String, Object> data = new HashMap<>();;
        data.put("id", "xxx");
        data.put("email", "xxx@gmail.com");
        data.put("password", "xxx");

        List list = new ArrayList<>();
        list.add(data);

        lightPostList.add(list);

        emptyRequestListAdapter = new listAdapter(new FrameLayout(new orgList().getApplicationContext()).getContext(),emptyPostList);
        lightRequestListAdapter = new listAdapter(new FrameLayout(new orgList().getApplicationContext()).getContext(),lightPostList);

    }

}
