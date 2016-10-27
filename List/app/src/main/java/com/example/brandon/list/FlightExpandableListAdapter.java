package com.example.brandon.list;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import JsonObjects.SeatMap;

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

    public void GetFlightDetails(final Context context)
    {
        final int numAvailableSeats;
        if(_listDataChild != null){
            for(int i = 0; i < _listDataChild.size(); i++){
                final List<FlightDetails> flights = _listDataChild.get(_listDataHeader.get(i));
                FlightItem flightItem = _listDataHeader.get(i);
                for(int j = 0; j < flights.size(); j++){
                    // Instantiate the RequestQueue.
                    com.android.volley.RequestQueue queue = Volley.newRequestQueue(context);
                    String url = context.getString(R.string.seat_map_url);
                    String departureDate = flightItem.flightDepartureTime.substring(0, 10);
                    String arrivalDate = flightItem.flightArrivalTime.substring(0, 10);

                    try {
                        com.android.volley.RequestQueue requestQueue = MainActivity.getInstance().getRequestQueue();
                        final SeatMap seatMap = new SeatMap(flightItem.flightArrival, flightItem.flightDeparture, departureDate, arrivalDate, flightItem.flightAirlineFlight);
                        final String mRequestBody = seatMap.GetSeatMapRequestBody();

                        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if(response != null){
                                    try{
                                        JSONObject object = response.getJSONObject("EnhancedSeatMapRS");
                                        JSONArray seatMapArray = object.getJSONArray("SeatMap");
                                        int numSeats = 0;
                                        String flightNumber = "";
                                        for(int i = 0; i < seatMapArray.length(); i++){
                                            JSONObject flight = seatMapArray.getJSONObject(i).getJSONObject("Flight");
                                            JSONArray flightMarketingArrayForFlightNumber = flight.getJSONArray("Marketing");
                                            for(int j = 0; j < flightMarketingArrayForFlightNumber.length(); j++){
                                                flightNumber = flightMarketingArrayForFlightNumber.getJSONObject(j).getString("content");
                                            }
                                            JSONArray cabinSeatMap = seatMapArray.getJSONObject(i).getJSONArray("Cabin");
                                            for(int j = 0; j < cabinSeatMap.length(); j++){
                                                JSONArray cabinRows = cabinSeatMap.getJSONObject(j).getJSONArray("Row");
                                                for(int k = 0; k < cabinRows.length(); k++){
                                                    JSONArray rowSeats = cabinRows.getJSONObject(k).getJSONArray("Seat");
                                                    for(int l = 0; l < rowSeats.length(); l++){
                                                        if(!rowSeats.getJSONObject(l).isNull("Occupation")) {
                                                            JSONArray occupiedSeat = rowSeats.getJSONObject(l).getJSONArray("Occupation");
                                                            for (int m = 0; m < occupiedSeat.length(); m++) {
                                                                JSONObject seatDetails = occupiedSeat.getJSONObject(m).getJSONObject("Detail");
                                                                if (seatDetails.getString("content").equalsIgnoreCase("SeatIsFree")) {
                                                                    numSeats += 1;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if(flightNumber.charAt(0) == '0'){
                                            flightNumber = flightNumber.substring(1, flightNumber.length());
                                        }
                                        for(int i = 0; i < flights.size(); i++){
                                            FlightDetails flightToUpdateSeats = flights.get(i);
                                            if(flightToUpdateSeats.flightDetailsFlightNumber.equalsIgnoreCase("399")){
                                                flightToUpdateSeats.flightDetailsSeatAvailability = String.valueOf(numSeats);
                                            }
                                        }
                                    } catch(Exception e){
                                        e.printStackTrace();
                                    }
                                }
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
                                params.put("Authorization", "Bearer " + context.getString(R.string.authentication_key));
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

        seatsAvailable.setText(childDetails.flightDetailsSeatAvailability);
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
