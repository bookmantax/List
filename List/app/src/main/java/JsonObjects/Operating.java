package JsonObjects;

/**
 * Created by Brandon on 10/25/2016.
 */

public class Operating {
    private String carrier;
    private String content;

    public Operating(String airline, String flightNumber){
        this.carrier = airline;
        this.content = flightNumber;
    }
}
