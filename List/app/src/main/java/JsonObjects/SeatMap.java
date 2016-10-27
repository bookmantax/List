package JsonObjects;

/**
 * Created by Brandon on 10/26/2016.
 */

public class SeatMap {
    private String seatMapRequestBody;

    public SeatMap(String destination, String origin, String departureDate, String arrivalDate, String carrier)
    {
        origin = "DFW";
        destination = "EZE";
        departureDate = "2017-01-07";
        arrivalDate = "2017-01-08";
        carrier = "AA";
        seatMapRequestBody = "{\n" +
                "\n" +
                "  \"EnhancedSeatMapRQ\": {\n" +
                "\n" +
                "    \"SeatMapQueryEnhanced\": {\n" +
                "\n" +
                "      \"RequestType\": \"Payload\",\n" +
                "\n" +
                "      \"Flight\": {\n" +
                "\n" +
                "        \"destination\": \"EZE\",\n" +
                "\n" +
                "        \"origin\": \"DFW\",\n" +
                "\n" +
                "      \"DepartureDate\": {\n" +
                "\n" +
                "        \"content\": \"2017-01-07\"\n" +
                "\n" +
                "      },\n" +
                "\n" +
                "      \"ArrivalDate\": {\n" +
                "\n" +
                "        \"content\": \"2017-01-08\"\n" +
                "\n" +
                "      },\n" +
                "\n" +
                "      \"Operating\": {\n" +
                "\n" +
                "        \"carrier\": \"AA\",\n" +
                "\n" +
                "        \"content\": \"997\"\n" +
                "\n" +
                "      },\n" +
                "\n" +
                "      \"Marketing\": [{\n" +
                "\n" +
                "        \"carrier\": \"AA\",\n" +
                "\n" +
                "        \"content\": \"997\"\n" +
                "\n" +
                "      }]\n" +
                "\n" +
                "      },\n" +
                "\n" +
                "    \"CabinDefinition\": {\n" +
                "\n" +
                "      \"RBD\": \"Y\"\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "  }\n" +
                "\n" +
                "}";
    }

    public String GetSeatMapRequestBody(){
        return seatMapRequestBody;
    }
}
