package com.example.brandon.list;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Brandon on 10/18/2016.
 */

public class SearchFragment extends android.support.v4.app.Fragment
{
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private EditText searchDepartingEditText, searchArrivingEditText;
    private DatePicker searchDatePicker;
    private Button searchSearchButton;
    private String departingLocation, arrivingLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = getActivity().getSharedPreferences("UserPreferences", 0);
        editor = settings.edit();
        departingLocation = settings.getString("DepartingLocation", "");
        arrivingLocation = settings.getString("ArrivingLocation", "");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        searchDepartingEditText = (EditText)rootView.findViewById(R.id.searchDepartingEditText);
        searchDepartingEditText.setText(departingLocation);
        searchArrivingEditText = (EditText)rootView.findViewById(R.id.searchArrivingEditText);
        searchArrivingEditText.setText(arrivingLocation);

        Calendar cal= Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);
        searchDatePicker = (DatePicker)rootView.findViewById(R.id.searchDatePicker);
        searchDatePicker.updateDate(year, month, day);

        searchSearchButton = (Button)rootView.findViewById(R.id.searchSearchButton);
        searchSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Submit();
            }
        });

        return rootView;
    }

    private void Submit()
    {
        if (NecessaryInformationProvided(departingLocation, arrivingLocation))
        {
            editor.putString("DepartingLocation", departingLocation);
            editor.putString("ArrivingLocation", arrivingLocation);
            editor.putInt("Year", searchDatePicker.getYear());
            editor.putInt("Month", searchDatePicker.getMonth());
            editor.putInt("Day", searchDatePicker.getDayOfMonth());
            editor.commit();
            ResultsFragment resultsFragment = new ResultsFragment();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, resultsFragment).commit();
        }
    }

    private boolean NecessaryInformationProvided(String departingLocation, String arrivingLocation)
    {
        if(departingLocation != null && arrivingLocation != null)
        {
            return true;
        }
        return false;
    }
}
