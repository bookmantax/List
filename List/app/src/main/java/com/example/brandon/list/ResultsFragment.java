package com.example.brandon.list;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.http.RequestQueue;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

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
        getFlights();
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

    private void getFlights() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url ="http://www.google.com";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        mTextView.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
