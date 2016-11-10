package com.example.brandon.list;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Brandon on 10/20/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Flight Table Variables Declaration
    static final String FLIGHT_ID = "flight_ID"; //PRIMARY KEY AUTOINCREMENT
    static final String DEPARTURE_TIME = "departure_time";
    static final String ARRIVAL_TIME = "arrival_time";
    static final String AIRLINE = "airline";
    static final String FLIGHT_NUMBER = "flight_number";
    static final String DEPARTURE_AIRPORT = "departure_airport";
    static final String ARRIVAL_AIRPORT = "arrival_airport";
    static final String ARRIVAL_DATE = "arrival_date";
    static final String DEPARTURE_DATE = "departure_date";
    static final String TRIP_ID = "trip_id";

    // Trip Table Variables Declaration
    static final String NUM_STOPS = "num_stops";
    static final String TRIP_DATE = "trip_date";

    // Database name and table
    private static final String DATABASE_NAME = "FCT_db";
    private static final String FLIGHT_TABLE_NAME = "flight_table";
    private static final String TRIP_TABLE_NAME = "trip_table";
    private static final String FAVORITES_TABLE_NAME = "favorites_table";
    private static final int DATABASE_VERSION = 1;

    // On Create Method
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creates the flight_table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + FLIGHT_TABLE_NAME +
                "(FLIGHT_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "DEPARTURE_TIME TEXT, " +
                "ARRIVAL_TIME TEXT, " +
                "AIRLINE TEXT, " +
                "FLIGHT_NUMBER INT, " +
                "DEPARTURE_AIRPORT TEXT, " +
                "ARRIVAL_AIRPORT TEXT, " +
                "ARRIVAL_DATE TEXT, " +
                "DEPARTURE_DATE TEXT");

        // Creates the favorites_table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + FAVORITES_TABLE_NAME +
                "(FAVORITES_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "AIRLINE TEXT, " +
                "FLIGHT_NUMBER INT, " +
                "DEPARTURE_AIRPORT TEXT, " +
                "ARRIVAL_AIRPORT TEXT");

        // Creates the trip_table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TRIP_TABLE_NAME +
                "(TRIP_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "NUM_STOPS INT, " +
                "DEPARTURE_AIRPORT TEXT, " +
                "ARRIVAL_AIRPORT TEXT, " +
                "TRIP_DATE TEXT");
    }

    // DB Constructor
    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //On Upgrade Method to drop the table if exist
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + FLIGHT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXIST " + TRIP_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXIST " + FAVORITES_TABLE_NAME);
        onCreate(db);
    }

    /**
     * Add trip to the Database
     */
    public boolean InsertFlight(String departureTime, String arrivalTime, int flightNumber, String airline,
                                String departureAirport, String arrivalAirport, String arrivalDate,
                                String departureDate){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEPARTURE_TIME, departureTime);
        contentValues.put(ARRIVAL_TIME, arrivalTime);
        contentValues.put(FLIGHT_NUMBER, flightNumber);
        contentValues.put(DEPARTURE_AIRPORT, departureAirport);
        contentValues.put(ARRIVAL_AIRPORT, arrivalAirport);
        contentValues.put(ARRIVAL_DATE, arrivalDate);
        contentValues.put(DEPARTURE_DATE, departureDate);
        contentValues.put(AIRLINE, airline);
        long result = db.insert(FLIGHT_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public boolean InsertFavoritesByFlightNumber(String airline, int flightNumber){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FLIGHT_NUMBER, flightNumber);
        contentValues.put(AIRLINE, airline);
        long result = db.insert(FAVORITES_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public boolean InsertFavoritesByDestination(String departureAirport, String arrivalAirport){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FLIGHT_NUMBER, -1);
        contentValues.put(DEPARTURE_AIRPORT, departureAirport);
        contentValues.put(ARRIVAL_AIRPORT, arrivalAirport);
        long result = db.insert(FAVORITES_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor GetFavorites(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor flights = db.rawQuery("select * from " + FAVORITES_TABLE_NAME, null);
        return flights;
    }

    public Cursor GetBookmarkedFlights(){
        SQLiteDatabase db = this.getWritableDatabase();
        DeleteOldFlights(db);
        Cursor flights = db.rawQuery("select * from " + FLIGHT_TABLE_NAME, null);
        return flights;
    }

    private void DeleteOldFlights(SQLiteDatabase db){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String todayString = df.format(c.getTime());
        Date today = new Date();
        try {
            today = df.parse(todayString);
        }catch (Exception ex) {}
        String tripString;
        Date tripDate;

        Cursor flights = db.rawQuery("select * from " + FLIGHT_TABLE_NAME, null);
        flights.moveToFirst();
        for(int i = 0; i < flights.getCount(); i++){
            tripString = df.format(flights.getString(flights.getColumnIndex("DEPARTURE_DATE")));
            try {
                tripDate = df.parse(tripString);
                if(tripDate.before(today))
                {
                    int flightId = flights.getInt(flights.getColumnIndex("FLIGHT_ID"));
                    db.delete(FLIGHT_TABLE_NAME, TRIP_ID + "=?", new String[]{String.valueOf(flightId)});
                }
            } catch (Exception e) {}
            flights.moveToNext();
        }
    }

    public boolean isFavoritesEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        String favorites = "SELECT count(*) FROM " + FAVORITES_TABLE_NAME;
        Cursor mcursor = db.rawQuery(favorites, null);
        mcursor.moveToFirst();
        return mcursor.getInt(0) == 0;
    }

    public boolean isFlightsEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        String flights = "SELECT count(*) FROM " + FLIGHT_TABLE_NAME;
        Cursor mcursor = db.rawQuery(flights, null);
        mcursor.moveToFirst();
        return mcursor.getInt(0) == 0;
    }

    /**
     * Add trip to the Database
     */
//    public boolean InsertTrip(int numStops, String departureAirport, String arrivalAirport, String tripDate){
//        SQLiteDatabase db = this.getReadableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(NUM_STOPS, numStops);
//        contentValues.put(DEPARTURE_AIRPORT, departureAirport);
//        contentValues.put(ARRIVAL_AIRPORT, arrivalAirport);
//        contentValues.put(TRIP_DATE, tripDate);
//        long result = db.insert(TRIP_TABLE_NAME, null, contentValues);
//        return result != -1;
//    }

//    public Cursor GetBookmarkedTrips() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        DeleteOldTrips(db);
//        Cursor trips = db.rawQuery("select * from " + TRIP_TABLE_NAME, null);
//        return trips;
//    }

//    public Cursor GetLastTrip() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor trips = db.rawQuery("select TOP(1) from " + TRIP_TABLE_NAME + " order by " +  TRIP_ID +
//                " desc", null);
//        return trips;
//    }

//    private void DeleteOldTrips(SQLiteDatabase db){
//        Calendar c = Calendar.getInstance();
//        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//        String todayString = df.format(c.getTime());
//        Date today = new Date();
//        try {
//            today = df.parse(todayString);
//        }catch (Exception ex) {}
//        String tripString;
//        Date tripDate;
//
//        Cursor trips = db.rawQuery("select * from " + TRIP_TABLE_NAME, null);
//        trips.moveToFirst();
//        for(int i = 0; i < trips.getCount(); i++){
//            tripString = df.format(trips.getString(trips.getColumnIndex("TRIP_DATE")));
//            try {
//                tripDate = df.parse(tripString);
//                if(tripDate.before(today))
//                {
//                    int tripId = trips.getInt(trips.getColumnIndex("TRIP_ID"));
//                    db.delete(TRIP_TABLE_NAME, TRIP_ID + "=?", new String[]{String.valueOf(tripId)});
//                    db.delete(FLIGHT_TABLE_NAME, TRIP_ID + "=?", new String[]{String.valueOf(tripId)});
//                }
//            } catch (Exception e) {}
//            trips.moveToNext();
//        }
//    }

//    public boolean isTripsEmpty() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        String trips = "SELECT count(*) FROM " + TRIP_TABLE_NAME;
//        Cursor mcursor = db.rawQuery(trips, null);
//        mcursor.moveToFirst();
//        return mcursor.getInt(0) == 0;
//    }


}
