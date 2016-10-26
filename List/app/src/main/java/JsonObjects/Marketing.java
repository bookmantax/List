package JsonObjects;

/**
 * Created by Brandon on 10/25/2016.
 */

public class Marketing {
    private String carrier;
    private String content;

    public Marketing(String airline, String flightNumber){
        this.carrier = airline;
        this.content = flightNumber;
    }
}
