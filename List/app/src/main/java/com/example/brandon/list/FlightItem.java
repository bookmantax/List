package com.example.brandon.list;

/**
 * Created by Brandon on 10/19/2016.
 */

public class FlightItem {
    public String flightDepartureTime, flightArrivalTime, flightAirlineFlight, flightDeparture,
            flightArrival, flightDate;
    public int flightStops, flightCapacity;

    public FlightItem(String flightDepartureTime, String flightArrivalTime, int flightStops, String flightAirlineFlight,
                      String flightDeparture, String flightArrival, int flightCapacity, String flightDate)
    {
        this.flightDepartureTime = flightDepartureTime;
        this.flightArrivalTime = flightArrivalTime;
        this.flightStops = flightStops;
        this.flightAirlineFlight = flightAirlineFlight;
        this.flightDeparture = flightDeparture;
        this.flightArrival = flightArrival;
        this.flightCapacity = flightCapacity;
        this.flightDate = flightDate;
    }

    public FlightItem(String flightDepartureTime, String flightArrivalTime, int flightStops,
                      String flightDeparture, String flightArrival, int flightCapacity, String flightDate)
    {
        this.flightDepartureTime = flightDepartureTime;
        this.flightArrivalTime = flightArrivalTime;
        this.flightStops = flightStops;
        this.flightDeparture = flightDeparture;
        this.flightArrival = flightArrival;
        this.flightCapacity = flightCapacity;
        this.flightAirlineFlight = "";
        this.flightDate = flightDate;
    }
}
