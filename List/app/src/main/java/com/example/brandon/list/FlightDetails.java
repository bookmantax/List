package com.example.brandon.list;

/**
 * Created by Brandon on 10/19/2016.
 */

public class FlightDetails {
    public String flightDetailsSeatAvailability, flightDetailsFlightNumber, flightDetailsDepartureTime,
    flightDetailsDepartureDate, flightDetailsDepartureAirport, flightDetailsArrivalTime, flightDetailsArrivalDate,
    flightDetailsArrivalAirport;

    public FlightDetails(String flightDetailsSeatAvailability, String flightDetailsFlightNumber, String flightDetailsDepartureTime,
                         String flightDetailsDepartureDate, String flightDetailsDepartureAirport, String flightDetailsArrivalTime,
                         String flightDetailsArrivalDate, String flightDetailsArrivalAirport)
    {
        this.flightDetailsSeatAvailability = flightDetailsSeatAvailability;
        this.flightDetailsFlightNumber = flightDetailsFlightNumber;
        this.flightDetailsDepartureTime = flightDetailsDepartureTime;
        this.flightDetailsDepartureDate = flightDetailsDepartureDate;
        this.flightDetailsDepartureAirport = flightDetailsDepartureAirport;
        this.flightDetailsArrivalTime = flightDetailsArrivalTime;
        this.flightDetailsArrivalDate = flightDetailsArrivalDate;
        this.flightDetailsArrivalAirport = flightDetailsArrivalAirport;
    }
}
