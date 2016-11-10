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
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private List<FlightDetails> flightDetailsList;
    private List<String> flightTimes, exclusiveAirlines;
    private FlightItem flightItem;
    private FlightDetails flightDetails;
    private String departingLocation, arrivingLocation, dateString, airline;
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
            GetFlights();
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

    private void GetFlights() {
        String url;
        com.android.volley.RequestQueue queue = MainActivity.getInstance().getRequestQueue();
        if(((MainActivity)getContext()).isFavoritesSearch){
            url = GetFlightsUrl(((MainActivity)getContext()).airlineOrOrigin, ((MainActivity)getContext()).flightNumberOrDestination,
                    String.valueOf(((MainActivity)getContext()).year) +  "-" + String.valueOf(((MainActivity)getContext()).month) + "-"
                            + String.valueOf(((MainActivity)getContext()).day));
        }
        else {
            url = GetFlightsUrl(settings.getString("DepartingLocation", ""),
                    settings.getString("ArrivingLocation", ""), String.valueOf(((MainActivity)getContext()).year) +  "-" +
                            String.valueOf(((MainActivity)getContext()).month) + "-" + String.valueOf(((MainActivity)getContext()).day));
        }
        //TODO handle morning/afternoon/night, direct only booleans, and exclusive airlines (exclusiveAirlines = Arrays.asList(str.split("\\s*,\\s*"));)
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (!jsonObject.getJSONArray("PricedItineraries").isNull(0)) {
                                    JSONArray allFlights = jsonObject.getJSONArray("PricedItineraries");
                                    for (int i = 0; i < allFlights.length(); i++) {
                                        JSONArray airItinerary = allFlights.getJSONObject(i).getJSONObject("AirItinerary")
                                                .getJSONObject("OriginDestinationOptions").getJSONArray("OriginDestinationOption");
                                        for (int j = 0; j < airItinerary.length(); j++) {
                                            JSONArray flightSegments = airItinerary.getJSONObject(j).getJSONArray("FlightSegment");
                                            flightTimes = new ArrayList<>();
                                            flightDetailsList = new ArrayList<>();
                                            for (int k = 0; k < flightSegments.length(); k++) {
                                                JSONObject flightObject = flightSegments.getJSONObject(k);
                                                flightTimes.add(flightObject.getString("DepartureDateTime"));
                                                flightTimes.add(flightObject.getString("ArrivalDateTime"));
                                                airline = flightObject.getJSONObject("MarketingAirline").getString("Code");
                                                //flightDetails = new FlightDetails("0", flightObject.getJSONObject("OperatingAirline").getString("FlightNumber"));
                                                flightDetailsList.add(flightDetails);
                                            }
                                            flightItem = new FlightItem(flightTimes, flightSegments.length(),
                                                    airline, jsonObject.getString("OriginLocation"), jsonObject.getString("DestinationLocation"),
                                                    0);
                                            listDataHeader.add(flightItem);
                                            listDataChild.put(flightItem, flightDetailsList);
                                        }
                                    }
                                }
                                flightAdapter = new FlightExpandableListAdapter(getContext(), listDataHeader, listDataChild);
                                resultsFlightsExpandableListView.setAdapter(flightAdapter);
                                flightAdapter.GetFlightDetails(getContext());
                                flightAdapter.notifyDataSetChanged();
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + getString(R.string.authentication_key));
                params.put("Accept", "*/*");

                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private String GetFlightsUrl(String origin, String destination, String departureDate)
    {
        return getString(R.string.flights_url) + "?origin=" + origin + "&destination=" +
                destination + "&departuredate=" + departureDate + "&returndate=2017-01-08&limit=1&onlineitinerariesonly=N&offset=1&eticketsonly=N&sortby=departuretime&order=asc&sortby2=elapsedtime&order2=asc&pointofsalecountry=US";
    }


    private void TestSoapCall(){
        final String SOAP_ACTION = "http://www.travelport.com/schema/air_v31_0/air:SeatMapReq";
        final String NAMESPACE = "http://www.travelport.com/schema/air_v31_0";
        final String METHOD_NAME = "air:SeatMapReq";
        final String URL = "https://americas.universal-api.pp.travelport.com/B2BGateway/connect/uAPI/";

        Thread nT = new Thread() {
            @Override
            public void run() {

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME);

                request.addProperty("userID", 1);


                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);

                envelope.setOutputSoapObject(request);
                envelope.dotNet = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(
                        URL);

                try {

                    androidHttpTransport.debug = true;
                    androidHttpTransport.call(SOAP_ACTION, envelope);

                    final String results = androidHttpTransport.responseDump.toString();
                    boolean res = results.equalsIgnoreCase("fail");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        };
        nT.start();
    }
}
