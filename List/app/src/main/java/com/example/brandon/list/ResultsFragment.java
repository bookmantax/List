package com.example.brandon.list;

import android.app.Application;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    private String departingLocation, arrivingLocation, dateString;
    private int year, month, day;
    private Date date = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = getActivity().getSharedPreferences("UserPreferences", 0);
        editor = settings.edit();
        departingLocation = settings.getString("DepartingLocation", "");
        arrivingLocation = settings.getString("ArrivingLocation", "");
        year = settings.getInt("Year", 0);
        month = settings.getInt("Month", 0);
        day = settings.getInt("Day", 0);
        if(year != 0 && month != 0 && day != 0)
        {
            date = new Date(year, month, day);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dateString = sdf.format(date);
        }
        if(date != null) {
            getFlights();
        }
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
        String url = getString(R.string.flights_search_url);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        if(response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if(!jsonObject.getJSONArray("PricedItineraries").isNull(0)){
                                    JSONArray allFlights = jsonObject.getJSONArray("PricedItineraries");
                                    for(int i = 0; i < allFlights.length(); i++){
                                        JSONArray airItinerary = allFlights.getJSONObject(i).getJSONObject("AirItinerary")
                                                .getJSONObject("OriginDestinationOptions").getJSONArray("OriginDestinationOption");
                                        for(int j = 0; j < airItinerary.length(); j++){
                                            JSONArray flightSegments = airItinerary.getJSONObject(j).getJSONArray("FlightSegment");
                                            for(int k = 0; k < flightSegments.length(); k++){
                                                JSONObject flightObject = flightSegments.getJSONObject(k);
                                                FlightItem flight = new FlightItem(flightObject.getString("DepartureDateTime"),
                                                        flightObject.getString("ArrivalDateTime"), flightSegments.length(),
                                                        flightObject.getString("MarketingAirline"), flightObject.getString("DepartureAirport"),
                                                        flightObject.getString("ArrivalAirport"), 0, flightObject.getString("MarketingAirline"));
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }

        }) {
            @Override
            protected Map<String,String> getParams() {
                // something to do here ??
                Map<String, String> params = new HashMap<>();
                params.put("origin", "JFK");
                params.put("destination", "LAX");
                params.put("departuredate", "2017-01-07");
                params.put("returndate", "2017-01-08");
                params.put("eticketsonly", "N");
                params.put("sortby", "departuretime");
                params.put("order", "asc");
                params.put("sortby2", "elapsedtime");
                params.put("order2", "asc");
                params.put("pointofsalecountry", "US");

                return params;
            }

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
