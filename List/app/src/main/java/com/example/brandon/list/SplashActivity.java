package com.example.brandon.list;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import java.util.List;
import java.util.Locale;

/**
 * Created by Brandon on 10/18/2016.
 */

public class SplashActivity extends AppCompatActivity {

    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final String[] INTERNET_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST=1337;
    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;
    String bestProvider, city, country;
    double latitude, longitude;
    Geocoder geocoder;
    List<Address> addresses;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    private GPSManager gpsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        DatabaseHelper db = new DatabaseHelper(this);
        if(!canAccessInternet()){
            requestPermissions(INTERNET_PERMS, INITIAL_REQUEST);
        }
        if (!canAccessLocation()) {
            requestPermissions(LOCATION_PERMS, INITIAL_REQUEST);
        }
        else
        {
            StartUp();
        }
    }

    private void StartUp()
    {
        settings = getSharedPreferences("UserPreferences", 0);
        editor = settings.edit();
        editor.putString("ArrivingLocation", "");
        editor.commit();

        gpsManager = new GPSManager(this);
        if (gpsManager.canGetLocation()) {
            latitude = gpsManager.getLatitude();
            longitude = gpsManager.getLongitude();
            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (Exception e) { }
            if (addresses != null && addresses.size() > 0)
            {
                city = addresses.get(0).getLocality();
                if(addresses.get(0).getCountryName().equalsIgnoreCase("United States"))
                {
                    country = addresses.get(0).getAdminArea();
                }
                else
                {
                    country = addresses.get(0).getCountryName();
                }
                editor.putString("DepartingLocation", city + ", " + country);
                editor.putFloat("Latitude", (float)latitude);
                editor.putFloat("Longitude", (float)longitude);
                editor.commit();

            }
            gpsManager.stopUsingGPS();
        }

        int secondsDelayed = 3;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (settings.getInt("UserId", 0) == 0)
                {
                    startActivity(new Intent(SplashActivity.this, SignupActivity.class));
                }
                else
                {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                finish();
            }
        }, secondsDelayed * 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch(requestCode) {

            case LOCATION_REQUEST:
                if (canAccessLocation()) {
                    StartUp();
                }
                else {

                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) || hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION));
    }

    private boolean canAccessInternet(){
        return(hasPermission(Manifest.permission.INTERNET));
    }

    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED==checkSelfPermission(perm));
    }
}
