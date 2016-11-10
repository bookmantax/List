package com.example.brandon.list;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AHBottomNavigation.OnTabSelectedListener
{
    private AHBottomNavigation bottomBar;
    private boolean startUp, search;
    private DatabaseHelper db;
    private static MainActivity sInstance;
    private RequestQueue mRequestQueue;
    public String airlineOrOrigin, flightNumberOrDestination;
    public int day, month, year;
    public boolean isFavoritesSearch, includeMorningResults, includeAfternoonResults, includeNightResults, directFlightsOnly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sInstance = this;
        startUp = true;
        search = false;
        db = new DatabaseHelper(this);
        isFavoritesSearch = false;
        includeMorningResults = true;
        includeAfternoonResults = true;
        includeNightResults = true;
        directFlightsOnly = false;

        Calendar cal= Calendar.getInstance();
        year=cal.get(Calendar.YEAR);
        month=cal.get(Calendar.MONTH);
        day=cal.get(Calendar.DAY_OF_MONTH);

        bottomBar = (AHBottomNavigation)findViewById(R.id.mainNavBar);
        bottomBar.setOnTabSelectedListener(this);
        this.CreateNavItems();
    }

    public static synchronized MainActivity getInstance() {
        return sInstance;
    }



    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    private void CreateNavItems()
    {
        AHBottomNavigationItem searchItem = new AHBottomNavigationItem("Search", R.drawable.ic_action_name);
        AHBottomNavigationItem resultsItem = new AHBottomNavigationItem("Results", R.drawable.ic_action_name);
        AHBottomNavigationItem bookmarksItem = new AHBottomNavigationItem("Bookmarks", R.drawable.ic_action_name);
        AHBottomNavigationItem hubItem = new AHBottomNavigationItem("Hub", R.drawable.ic_action_name);
        AHBottomNavigationItem settingsItem = new AHBottomNavigationItem("Settings", R.drawable.ic_action_name);

        bottomBar.addItem(searchItem);
        bottomBar.addItem(resultsItem);
        bottomBar.addItem(bookmarksItem);
        bottomBar.addItem(hubItem);
        bottomBar.addItem(settingsItem);

        bottomBar.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));
        bottomBar.setCurrentItem(0);
    }

    @Override
    public void onTabSelected(int position, boolean wasSelected)
    {
        if(position == 0 && (startUp || !wasSelected))
        {
            startUp = false;
            SearchFragment searchFragment = new SearchFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, searchFragment).commit();
        }
        else if(position == 1 && !wasSelected)
        {
            ResultsFragment resultsFragment = new ResultsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, resultsFragment).commit();
        }
        else if(position == 2 && !wasSelected)
        {
            BookmarksFragment bookmarksFragment = new BookmarksFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, bookmarksFragment).commit();
        }
        else if(position == 3 && !wasSelected)
        {
            HubFragment hubFragment = new HubFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, hubFragment).commit();
        }
        else if(position == 4 && !wasSelected)
        {
            SettingsFragment settingsFragment = new SettingsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, settingsFragment).commit();
        }
    }

    public void SetStartUp(boolean bool)
    {
        startUp = bool;
    }

    public void SetCurrentTab(int pos)
    {
        bottomBar.setCurrentItem(pos);
    }

    public void SetSearch(boolean bool)
    {
        search = bool;
    }
}
