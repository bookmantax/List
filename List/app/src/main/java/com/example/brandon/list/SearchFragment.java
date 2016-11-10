package com.example.brandon.list;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Brandon on 10/18/2016.
 */

public class SearchFragment extends android.support.v4.app.Fragment
{
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private EditText searchDepartingEditText, searchArrivingEditText;
    private TextView searchDateDepartingTextView;
    private Button searchSearchButton, searchChangeDateButton;
    private ToggleButton searchMorningToggleButton, searchAfternoonToggleButton, searchNightToggleButton;
    private CheckBox searchDirectFlightCheckBox;
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

        searchDateDepartingTextView = (TextView)rootView.findViewById(R.id.searchDateDepartingTextView);
        searchDateDepartingTextView.setText(((MainActivity)getContext()).month + "-" + ((MainActivity)getContext()).day + "-" + ((MainActivity)getContext()).year);

        searchChangeDateButton = (Button)rootView.findViewById(R.id.searchChangeDateButton);
        searchChangeDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String date = String.valueOf(month) +"/"+String.valueOf(day)
                                +"/"+String.valueOf(year);
                        searchDateDepartingTextView.setText(date);
                        ((MainActivity)getContext()).year = year;
                        ((MainActivity)getContext()).month = month;
                        ((MainActivity)getContext()).day = day;
                    }
                }, ((MainActivity)getContext()).year, ((MainActivity)getContext()).month, ((MainActivity)getContext()).day);
                datePicker.show();
            }
        });

        searchSearchButton = (Button)rootView.findViewById(R.id.searchSearchButton);
        searchSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Submit();
            }
        });

        searchMorningToggleButton = (ToggleButton)rootView.findViewById(R.id.searchMorningToggleButton);
        searchMorningToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    buttonView.setBackgroundColor(Color.LTGRAY);
                    ((MainActivity)getContext()).includeMorningResults = true;
                }
                else {
                    buttonView.setBackgroundColor(Color.WHITE);
                    ((MainActivity)getContext()).includeMorningResults = false;
                }
            }
        });
        searchAfternoonToggleButton = (ToggleButton)rootView.findViewById(R.id.searchAfternoonToggleButton);
        searchAfternoonToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    buttonView.setBackgroundColor(Color.LTGRAY);
                    ((MainActivity)getContext()).includeAfternoonResults = true;
                }
                else {
                    buttonView.setBackgroundColor(Color.WHITE);
                    ((MainActivity)getContext()).includeAfternoonResults = false;
                }
            }
        });
        searchNightToggleButton = (ToggleButton)rootView.findViewById(R.id.searchNightToggleButton);
        searchNightToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    buttonView.setBackgroundColor(Color.LTGRAY);
                    ((MainActivity)getContext()).includeNightResults = true;
                }
                else {
                    buttonView.setBackgroundColor(Color.WHITE);
                    ((MainActivity)getContext()).includeNightResults = false;
                }
            }
        });

        searchDirectFlightCheckBox = (CheckBox)rootView.findViewById(R.id.searchDirectCheckBox);

        return rootView;
    }

    private void Submit()
    {
        ((MainActivity)getContext()).isFavoritesSearch = false;
        departingLocation = String.valueOf(searchDepartingEditText.getText());
        arrivingLocation = String.valueOf(searchArrivingEditText.getText());
        ((MainActivity)getContext()).directFlightsOnly = searchDirectFlightCheckBox.isChecked();
        if (NecessaryInformationProvided(departingLocation, arrivingLocation))
        {
            editor.putString("DepartingLocation", departingLocation);
            editor.putString("ArrivingLocation", arrivingLocation);
            editor.commit();
            ResultsFragment resultsFragment = new ResultsFragment();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, resultsFragment).commit();
        }
    }

    private boolean NecessaryInformationProvided(String departingLocation, String arrivingLocation)
    {
        return departingLocation != "" && arrivingLocation != "";
    }
}
