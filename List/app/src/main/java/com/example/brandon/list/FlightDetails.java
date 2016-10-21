package com.example.brandon.list;

/**
 * Created by Brandon on 10/19/2016.
 */

public class FlightDetails {
    public String flightDetailsSeatAvailabilityTextView, flightDetailsDepartureAirport, flightDetailsArrivalAirport,
    flightDetailsDepartureTime, flightDetailsArrivalTime, flightDetailsAirline;
    public int flightDetailsFlightNumber;

    public FlightDetails(String flightDetailsSeatAvailabilityTextView, String flightDetailsDepartureAirport, String flightDetailsArrivalAirport,
                         String flightDetailsDepartureTime, String flightDetailsArrivalTime, String flightDetailsAirline, int flightDetailsFlightNumber)
    {
        this.flightDetailsSeatAvailabilityTextView = flightDetailsSeatAvailabilityTextView;
        this.flightDetailsDepartureAirport = flightDetailsDepartureAirport;
        this.flightDetailsArrivalAirport = flightDetailsArrivalAirport;
        this.flightDetailsDepartureTime = flightDetailsDepartureTime;
        this.flightDetailsArrivalTime = flightDetailsArrivalTime;
        this.flightDetailsAirline = flightDetailsAirline;
        this.flightDetailsFlightNumber = flightDetailsFlightNumber;
    }
}
