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

import java.util.List;


public class MainActivity extends AppCompatActivity implements AHBottomNavigation.OnTabSelectedListener
{
    private AHBottomNavigation bottomBar;
    private boolean startUp, search;
    private DatabaseHelper db;
    private static MainActivity sInstance;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sInstance = this;
        startUp = true;
        search = false;
        db = new DatabaseHelper(this);

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
        AHBottomNavigationItem resultsItem = new AHBottomNavigationItem("Users", R.drawable.ic_action_name);
        AHBottomNavigationItem bookmarksItem = new AHBottomNavigationItem("CheckIn", R.drawable.ic_action_name);
        AHBottomNavigationItem hubItem = new AHBottomNavigationItem("Rating", R.drawable.ic_action_name);

        bottomBar.addItem(searchItem);
        bottomBar.addItem(resultsItem);
        bottomBar.addItem(bookmarksItem);
        bottomBar.addItem(hubItem);

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
            ResultsFragment usersFragment = new ResultsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, usersFragment).commit();
        }
        else if(position == 2 && !wasSelected)
        {
            BookmarksFragment checkInFragment = new BookmarksFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, checkInFragment).commit();
        }
        else if(position == 3 && !wasSelected)
        {
            HubFragment ratingFragment = new HubFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, ratingFragment).commit();
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

    public void CreateBookmark(FlightItem flightItem, List<FlightDetails> flightDetails)
    {
        Cursor lastTrip;
        FlightDetails flightDetail;
        int lastTripId;
        String departureDate = flightItem.flightDepartureTime.substring(0, 10);
        String arrivalDate = flightItem.flightArrivalTime.substring(0, 10);
        db.InsertTrip(flightDetails.size(), flightItem.flightDeparture, flightItem.flightArrival,
                flightItem.flightDepartureTime, flightItem.flightArrivalTime, departureDate + " - " + arrivalDate);

        lastTrip = db.GetLastTrip();
        lastTrip.moveToFirst();
        lastTripId = Integer.parseInt(lastTrip.getString(lastTrip.getColumnIndex("TRIP_ID")));

        for(int i = 0; i < flightDetails.size(); i++)
        {
            flightDetail = flightDetails.get(i);
            db.InsertFlight(Integer.getInteger(flightDetail.flightDetailsFlightNumber), lastTripId);
        }
    }
}
