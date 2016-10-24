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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://api.test.sabre.com/v1/shop/flights?origin=JFK&destination=LAX&departuredate=2017-01-07&returndate=2017-01-08&onlineitinerariesonly=N&limit=10&offset=1&eticketsonly=N&sortby=totalfare&order=asc&sortby2=departuretime&order2=asc&pointofsalecountry=US";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        String s = response;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + "T1RLAQI4vAqKEO6jbHI1B/j0PSl9R6eM7xCoq4/vHHRa4JvJumzHItcqAACgPZIoCFBPutU1Qau9j9QkbKpdpqQVj820GWhdoZ9sgSPZGL8RISZWhvL3wCKVR+t4U79B6ahBi6AGDwX8bDPwH13XEB5q0F/z9nrqX9NCmZP8pChOT8YKxUO+F2Tzxxo1BwMtpa2rfwEB/4W/BTbXqC77wGlhS/kFsKeJD8yiekAOBd27Ub5HXkvICZqnzTAd1XtTZXEAgUigl7mc3CBM7g**");
                params.put("Accept", "*/*");

                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
