package com.example.brandon.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Brandon on 10/19/2016.
 */

public class FlightExpandableListAdapter extends BaseExpandableListAdapter {

    private final Context _context;
    private final List<FlightItem> _listDataHeader;
    private final HashMap<FlightItem, List<FlightDetails>> _listDataChild;

    public FlightExpandableListAdapter(Context context, List<FlightItem> listDataHeader,
                                       HashMap<FlightItem, List<FlightDetails>> listDataChild)
    {
        this._context = context;
        this._listDataChild = listDataChild;
        this._listDataHeader = listDataHeader;
    }

    public void GetFlightDetails(Context context)
    {
        if(_listDataChild != null){
            for(int i = 0; i < _listDataChild.size(); i++){
                List<FlightDetails> flights = _listDataChild.get(_listDataHeader.get(i));
                for(int j = 0; j < flights.size(); j++){
                    // Instantiate the RequestQueue.
                    com.android.volley.RequestQueue queue = Volley.newRequestQueue(context);
                    String url = "https://api.test.sabre.com/v1/shop/flights?origin=JFK&destination=LAX&departuredate=2017-01-07&returndate=2017-01-08&onlineitinerariesonly=N&limit=10&offset=1&eticketsonly=N&sortby=totalfare&order=asc&sortby2=departuretime&order2=asc&pointofsalecountry=US";

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Display the first 500 characters of the response string.
                                    if(response != null) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);

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
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) { return childPosition;}

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent)
    {
        final FlightDetails childDetails = (FlightDetails) getChild(groupPosition, childPosition);
        final FlightItem flightItem = (FlightItem) getGroup(groupPosition);

        LayoutInflater infalInflater = (LayoutInflater) this._context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View row = infalInflater.inflate(R.layout.flight_details, null);

        final TextView seatsAvailable = (TextView) row
                .findViewById(R.id.flightDetailsSeatAvailabilityTextView);
        final Button bookmarkButtons = (Button) row
                .findViewById(R.id.flightDetailsBookmarkButton);

        seatsAvailable.setText(childDetails.flightDetailsSeatAvailabilityTextView);
        if(flightItem.flightArrival == "") {
            bookmarkButtons.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO save flight in bookmarks table of phone database
                }
            });
        } else {
            bookmarkButtons.setVisibility(View.GONE);
        }
        return row;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        FlightItem flightItem = (FlightItem) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.flight_item, null);
        }

        TextView flightTimesTextView = (TextView) convertView
                .findViewById(R.id.flightTimesTextView);
        String departure = flightItem.flightDepartureTime;
        int departureTimeLength = departure.length();
        String arrival = flightItem.flightArrivalTime;
        int arrivalTimeLength = arrival.length();
        flightTimesTextView.setText(departure.substring(departureTimeLength - 8, departureTimeLength) + " - " + arrival.substring(arrivalTimeLength - 8, arrivalTimeLength));

        TextView flightStopsTextView = (TextView) convertView
                .findViewById(R.id.flightStopsTextView);
        flightStopsTextView.setText(String.valueOf(flightItem.flightStops));

        TextView flightAirlineFlight = (TextView) convertView
                .findViewById(R.id.flightAirlineFlightTextView);
        flightAirlineFlight.setText(flightItem.flightAirlineFlight);

        TextView flightCapacity = (TextView) convertView
                .findViewById(R.id.flightCapacityTextView);
        flightCapacity.setText(String.valueOf(flightItem.flightCapacity));

        TextView flightDepartureArrival = (TextView) convertView
                .findViewById(R.id.flightDepartureArrivalTextView);
        flightDepartureArrival.setText(flightItem.flightDeparture + " - " + flightItem.flightArrival);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
