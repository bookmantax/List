package com.example.brandon.list;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Brandon on 10/18/2016.
 */

public class BookmarksFragment extends android.support.v4.app.Fragment
{
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private ExpandableListView bookmarksExpandableListView;
    private DatabaseHelper db = null;
    private FlightExpandableListAdapter flightAdapter;
    private List<FlightItem> listDataHeader;
    private HashMap<FlightItem, List<FlightDetails>> listDataChild;
    private List<FlightDetails> flightDetailsList;
    private List<String> flightTimes;
    private FlightItem flightItem;
    private FlightDetails flightDetails;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(db == null){
            db = new DatabaseHelper(getContext());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        bookmarksExpandableListView = (ExpandableListView)rootView.findViewById(R.id.bookmarksFlightsExpandableListView);
        GetBookmarkedFlightsAndCreateAdapter();
        bookmarksExpandableListView.setAdapter(flightAdapter);

        return rootView;
    }

    public void GetBookmarkedFlightsAndCreateAdapter(){
        if(!db.isFlightsEmpty()) {
            listDataHeader = new ArrayList<>();
            listDataChild = new HashMap<>();
            Cursor bookmarkedFlights = db.GetBookmarkedFlights();
            bookmarkedFlights.moveToFirst();
            for(int i = 0; i < bookmarkedFlights.getCount(); i++) {
                flightTimes = new ArrayList<>();
                flightTimes.add(bookmarkedFlights.getString(bookmarkedFlights.getColumnIndex("DEPARTURE_TIME")));
                flightTimes.add(bookmarkedFlights.getString(bookmarkedFlights.getColumnIndex("ARRIVAL_TIME")));
                flightItem = new FlightItem(flightTimes, 0,
                        bookmarkedFlights.getString(bookmarkedFlights.getColumnIndex("AIRLINE")),
                        bookmarkedFlights.getString(bookmarkedFlights.getColumnIndex("DEPARTURE_AIRPORT")),
                        bookmarkedFlights.getString(bookmarkedFlights.getColumnIndex("ARRIVAL_AIRPORT")),
                        0);
                flightDetailsList = new ArrayList<>();
                flightDetails = new FlightDetails("0", String.valueOf(bookmarkedFlights.getInt(bookmarkedFlights.getColumnIndex("FLIGHT_NUMBER"))),
                        bookmarkedFlights.getString(bookmarkedFlights.getColumnIndex("DEPARTURE_TIME")),
                        bookmarkedFlights.getString(bookmarkedFlights.getColumnIndex("DEPARTURE_DATE")),
                        bookmarkedFlights.getString(bookmarkedFlights.getColumnIndex("DEPARTURE_AIRPORT")),
                        bookmarkedFlights.getString(bookmarkedFlights.getColumnIndex("ARRIVAL_TIME")),
                        bookmarkedFlights.getString(bookmarkedFlights.getColumnIndex("ARRIVAL_DATE")),
                        bookmarkedFlights.getString(bookmarkedFlights.getColumnIndex("ARRIVAL_AIRPORT")));
                flightDetailsList.add(flightDetails);
                listDataHeader.add(flightItem);
                listDataChild.put(flightItem, flightDetailsList);
                bookmarkedFlights.moveToNext();
            }
            flightAdapter = new FlightExpandableListAdapter(getContext(), listDataHeader, listDataChild);
        }
    }
}
