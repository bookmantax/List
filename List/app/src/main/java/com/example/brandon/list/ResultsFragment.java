package com.example.brandon.list;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import JsonObjects.ArrivalDate;
import JsonObjects.CabinDefinition;
import JsonObjects.DepartureDate;
import JsonObjects.EnhancedSeatMapRQ;
import JsonObjects.Flight;
import JsonObjects.Marketing;
import JsonObjects.Operating;
import JsonObjects.SeatMapQueryEnhanced;

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
    private FlightItem flightItem;
    private FlightDetails flightDetails;
    private String departingLocation, arrivingLocation, dateString;
    private int year, month, day;
    private Date date = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = getActivity().getSharedPreferences("UserPreferences", 0);
        editor = settings.edit();
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
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
        final View rootView = inflater.inflate(R.layout.fragment_results, container, false);

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
        String url = "https://api.test.sabre.com/v4.0.0/book/flights/seatmaps?mode=seatmaps";

        DepartureDate departureDate = new DepartureDate("2017-01-07");
        ArrivalDate arrivalDate = new ArrivalDate("2017-01-08");
        Operating operating = new Operating("AA", "997");
        List<Marketing> marketingList = new ArrayList<>();
        Marketing marketing = new Marketing("AA", "997");
        marketingList.add(marketing);
        CabinDefinition cabinDefinition = new CabinDefinition();
        Flight flight = new Flight("EZE", "DFW", departureDate, arrivalDate, operating, marketingList);
        SeatMapQueryEnhanced seatMapQueryEnhanced = new SeatMapQueryEnhanced("Payload", flight, cabinDefinition);
        final EnhancedSeatMapRQ enhancedSeatMapRQ = new EnhancedSeatMapRQ(seatMapQueryEnhanced);

        Map<String, Object> params = new HashMap<>();
        params.put("EnhancedSeatMapRQ", enhancedSeatMapRQ);

        try {
            com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            Gson gson = new Gson();
            final String mRequestBody = "{\n" +
                    "\n" +
                    "  \"EnhancedSeatMapRQ\": {\n" +
                    "\n" +
                    "    \"SeatMapQueryEnhanced\": {\n" +
                    "\n" +
                    "      \"RequestType\": \"Payload\",\n" +
                    "\n" +
                    "      \"Flight\": {\n" +
                    "\n" +
                    "        \"destination\": \"EZE\",\n" +
                    "\n" +
                    "        \"origin\": \"DFW\",\n" +
                    "\n" +
                    "      \"DepartureDate\": {\n" +
                    "\n" +
                    "        \"content\": \"2017-01-07\"\n" +
                    "\n" +
                    "      },\n" +
                    "\n" +
                    "      \"ArrivalDate\": {\n" +
                    "\n" +
                    "        \"content\": \"2017-01-08\"\n" +
                    "\n" +
                    "      },\n" +
                    "\n" +
                    "      \"Operating\": {\n" +
                    "\n" +
                    "        \"carrier\": \"AA\",\n" +
                    "\n" +
                    "        \"content\": \"997\"\n" +
                    "\n" +
                    "      },\n" +
                    "\n" +
                    "      \"Marketing\": [{\n" +
                    "\n" +
                    "        \"carrier\": \"AA\",\n" +
                    "\n" +
                    "        \"content\": \"997\"\n" +
                    "\n" +
                    "      }]\n" +
                    "\n" +
                    "      },\n" +
                    "\n" +
                    "    \"CabinDefinition\": {\n" +
                    "\n" +
                    "      \"RBD\": \"Y\"\n" +
                    "\n" +
                    "    }\n" +
                    "\n" +
                    "    }\n" +
                    "\n" +
                    "  }\n" +
                    "\n" +
                    "}";

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("VOLLEY", response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        uee.printStackTrace();
                        return null;
                    }
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Bearer " + "T1RLAQLL0SbnLEg59pULOPhiuEfngaZBQBC/2aN2fFY+cqW/tF5vpd23AACgnyHiES1UgKwDeyNBbeOznfO0s9jkobJjX+LsyYXt42ml+x/gIEa9SEr6tzaLeSt+2X/QVC1zgRguWa93S6jGyxSUqunkgNfwyfBdT7u/EJ/dpMjdGN3qk24E9TUk6vgRPXKiZiRqzlqNWEZgGiCj8dSDV3Qt5SsxxdK1WlRLt8DJJ4y5x0LhP3UeD/TxUNBIOV0ujHKlHM+kFuHK5EWmiw**");
                    params.put("Accept", "*/*");

                    return params;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    60000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }


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
                                                flightItem = new FlightItem(flightObject.getString("DepartureDateTime"),
                                                        flightObject.getString("ArrivalDateTime"), flightSegments.length(),
                                                        flightObject.getJSONObject("MarketingAirline").getString("Code"), flightObject.getJSONObject("DepartureAirport").getString("LocationCode"),
                                                        flightObject.getJSONObject("ArrivalAirport").getString("LocationCode"), 0, flightObject.getJSONObject("MarketingAirline").getString("Code"));
                                                listDataHeader.add(flightItem);
                                            }
                                        }
                                    }
                                }
                                flightAdapter = new FlightExpandableListAdapter(getContext(), listDataHeader, listDataChild);
                                resultsFlightsExpandableListView.setAdapter(flightAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }

        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("grant_type", "password");
                params.put("username", "User0");
                params.put("password", "Password0");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + "T1RLAQLL0SbnLEg59pULOPhiuEfngaZBQBC/2aN2fFY+cqW/tF5vpd23AACgnyHiES1UgKwDeyNBbeOznfO0s9jkobJjX+LsyYXt42ml+x/gIEa9SEr6tzaLeSt+2X/QVC1zgRguWa93S6jGyxSUqunkgNfwyfBdT7u/EJ/dpMjdGN3qk24E9TUk6vgRPXKiZiRqzlqNWEZgGiCj8dSDV3Qt5SsxxdK1WlRLt8DJJ4y5x0LhP3UeD/TxUNBIOV0ujHKlHM+kFuHK5EWmiw**");
                params.put("Accept", "*/*");

                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
