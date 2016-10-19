package com.example.brandon.list;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Brandon on 10/18/2016.
 */

public class ResultsFragment extends android.support.v4.app.Fragment
{
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private TextView resultsDepartureTextView, resultsArrivingTextView;
    private ExpandableListView resultsFlightsExpandableListView;
    private FlightExpandableListAdapter flightAdapter;
    private List<FlightItem> listDataHeader;
    private HashMap<FlightItem, List<FlightDetails>> listDataChild;
    private List<FlightDetails> ratings;
    private FlightItem expItem;
    private FlightDetails expDetails;
    private String departingLocation, arrivingLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = getActivity().getSharedPreferences("UserPreferences", 0);
        editor = settings.edit();
        departingLocation = settings.getString("DepartingLocation", "");
        arrivingLocation = settings.getString("ArrivingLocation", "");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        resultsDepartureTextView = (TextView)rootView.findViewById(R.id.resultsDepartureTextView);
        resultsDepartureTextView.setText(departingLocation);

        resultsArrivingTextView = (TextView)rootView.findViewById(R.id.resultsArrivingTextView);
        resultsArrivingTextView.setText(arrivingLocation);

        resultsFlightsExpandableListView = (ExpandableListView)rootView.findViewById(R.id.resultsFlightsExpandableListView);

        return rootView;
    }
}
