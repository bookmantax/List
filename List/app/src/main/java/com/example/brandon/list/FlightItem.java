package com.example.brandon.list;

import java.util.List;

import static android.R.id.list;

/**
 * Created by Brandon on 10/19/2016.
 */

public class FlightItem {
    public String flightDepartureTime, flightArrivalTime, flightAirlineFlight, flightDeparture,
            flightArrival;
    public int flightStops, flightCapacity;

    public FlightItem(List<String> flightTimes, int flightStops, String flightAirlineFlight,
                      String flightDeparture, String flightArrival, int flightCapacity)
    {
        this.flightDepartureTime = flightTimes.get(0);
        this.flightArrivalTime = flightTimes.get(flightTimes.size() - 1);
        this.flightStops = flightStops;
        this.flightAirlineFlight = flightAirlineFlight;
        this.flightDeparture = flightDeparture;
        this.flightArrival = flightArrival;
        this.flightCapacity = flightCapacity;
    }

    public FlightItem(String flightDepartureTime, String flightArrivalTime, int flightStops,
                      String flightDeparture, String flightArrival, int flightCapacity)
    {
        this.flightDepartureTime = flightDepartureTime;
        this.flightArrivalTime = flightArrivalTime;
        this.flightStops = flightStops;
        this.flightDeparture = flightDeparture;
        this.flightArrival = flightArrival;
        this.flightCapacity = flightCapacity;
        this.flightAirlineFlight = "";
    }
}
