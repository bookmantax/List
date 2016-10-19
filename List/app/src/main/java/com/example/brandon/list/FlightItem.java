package com.example.brandon.list;

/**
 * Created by Brandon on 10/19/2016.
 */

public class FlightItem {
    public String flightTimes, flightStops, flightAirlineFlight, flightDepartureArrival, flightCapacity;

    public FlightItem(String flightTimes, String flightStops, String flightAirlineFlight,
                      String flightDepartureArrival, String flightCapacity)
    {
        this.flightTimes = flightTimes;
        this.flightStops = flightStops;
        this.flightAirlineFlight = flightAirlineFlight;
        this.flightDepartureArrival = flightDepartureArrival;
        this.flightCapacity = flightCapacity;
    }

    public FlightItem(String flightTimes, String flightStops,
                      String flightDepartureArrival, String flightCapacity)
    {
        this.flightTimes = flightTimes;
        this.flightStops = flightStops;
        this.flightDepartureArrival = flightDepartureArrival;
        this.flightCapacity = flightCapacity;
        this.flightAirlineFlight = "";
    }
}
