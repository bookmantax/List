package JsonObjects;

import java.util.List;

/**
 * Created by Brandon on 10/25/2016.
 */

public class Flight {
    private String destination;
    private String origin;
    private DepartureDate departureDate;
    private ArrivalDate arrivalDate;
    private Operating operating;
    private List<Marketing> marketing;

    public Flight(String destination, String origin, DepartureDate departureDate, ArrivalDate arrivalDate,
                  Operating operating, List<Marketing> marketing)
    {
        this.destination = destination;
        this.origin = origin;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.operating = operating;
        this.marketing = marketing;
    }
}
