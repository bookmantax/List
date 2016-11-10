package com.example.brandon.list;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Brandon on 11/9/2016.
 */

public class SettingsFragment extends android.support.v4.app.Fragment
{
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private EditText settingsAirlineEditText;
    private TextView settingsAirlinesIncludedTextView;
    private Button settingsAddAirlineButton, settingsClearAirlinesButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = getActivity().getSharedPreferences("UserPreferences", 0);
        editor = settings.edit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        settingsAirlineEditText = (EditText)rootView.findViewById(R.id.settingsAirlineEditText);
        settingsAirlinesIncludedTextView = (TextView)rootView.findViewById(R.id.settingsAirlinesIncludedTextView);
        settingsAirlinesIncludedTextView.setText(settings.getString("ExclusiveAirlines", ""));
        settingsAddAirlineButton = (Button)rootView.findViewById(R.id.settingsAddAirlineButton);
        settingsAddAirlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAirline();
            }
        });
        settingsClearAirlinesButton = (Button)rootView.findViewById(R.id.settingsClearAirlinesButton);
        settingsClearAirlinesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearAirlines();
            }
        });

        return rootView;
    }

    private void AddAirline(){
        String previousAirlines = settings.getString("ExclusiveAirlines", "");
        editor.putString("ExclusiveAirlines", previousAirlines + ", " + String.valueOf(settingsAirlineEditText.getText()));
    }

    private void ClearAirlines(){
        editor.remove("ExclusiveAirlines");
    }
}
