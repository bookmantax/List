package com.example.brandon.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

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
        if(flightItem.flightDepartureArrival == "") {
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
        flightTimesTextView.setText(flightItem.flightTimes);

        TextView flightStopsTextView = (TextView) convertView
                .findViewById(R.id.flightStopsTextView);
        flightStopsTextView.setText(flightItem.flightStops);

        TextView flightAirlineFlight = (TextView) convertView
                .findViewById(R.id.flightAirlineFlightTextView);
        flightAirlineFlight.setText(flightItem.flightAirlineFlight);

        TextView flightCapacity = (TextView) convertView
                .findViewById(R.id.flightCapacityTextView);
        flightCapacity.setText(flightItem.flightCapacity);

        TextView flightDepartureArrival = (TextView) convertView
                .findViewById(R.id.flightDepartureArrivalTextView);
        flightDepartureArrival.setText(flightItem.flightDepartureArrival);

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
