package JsonObjects;

/**
 * Created by Brandon on 10/25/2016.
 */

public class SeatMapQueryEnhanced {
    private String RequestType;
    private Flight flight;
    private CabinDefinition cabinDefinition;

    public SeatMapQueryEnhanced(String requestType, Flight flight, CabinDefinition cabinDefinition)
    {
        this.RequestType = requestType;
        this.flight = flight;
        this.cabinDefinition = cabinDefinition;
    }
}
