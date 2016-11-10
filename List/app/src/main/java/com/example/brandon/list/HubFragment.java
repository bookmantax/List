package com.example.brandon.list;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Brandon on 10/18/2016.
 */

public class HubFragment extends android.support.v4.app.Fragment
{
    private Button favoritesAddFavoriteButton, favoritesChangeDateButton;
    private ListView favoritesResultsListView;
    private TextView favoritesDateTextView;
    private DatabaseHelper db;
    private FavoriteItem[] favoritesArray;
    private FavoriteListAdapter favoriteAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(db == null){
            db = new DatabaseHelper(getContext());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_hub, container, false);

        favoritesDateTextView = (TextView)rootView.findViewById(R.id.favoritesDateTextView);
        favoritesDateTextView.setText(((MainActivity)getContext()).month + "/" + ((MainActivity)getContext()).day + "/" + ((MainActivity)getContext()).year);

        favoritesAddFavoriteButton = (Button)rootView.findViewById(R.id.favoritesAddFavoriteButton);
        favoritesAddFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFavoriteTrip();
            }
        });

        favoritesChangeDateButton = (Button)rootView.findViewById(R.id.favoritesChangeDateButton);
        favoritesChangeDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String date = String.valueOf(month) +"-"+String.valueOf(day)
                                +"-"+String.valueOf(year);
                        favoritesDateTextView.setText(date);
                        ((MainActivity)getContext()).year = year;
                        ((MainActivity)getContext()).month = month;
                        ((MainActivity)getContext()).day = day;
                    }
                }, ((MainActivity)getContext()).year, ((MainActivity)getContext()).month, ((MainActivity)getContext()).day);
                datePicker.show();
            }
        });

        favoritesResultsListView = (ListView)rootView.findViewById(R.id.favoritesResultsListView);
        GetFavorites();
        favoritesResultsListView.setAdapter(favoriteAdapter);

        return rootView;
    }

    private void GetFavorites()
    {
        int flightNumber;
        FavoriteItem favorite;
        if(!db.isFavoritesEmpty()) {
            Cursor favorites = db.GetFavorites();
            favorites.moveToFirst();
            favoritesArray = new FavoriteItem[favorites.getCount()];
            for(int i = 0; i < favorites.getCount(); i++){
                flightNumber = Integer.getInteger(favorites.getString(favorites.getColumnIndex("FLIGHT_NUMBER")));
                if(flightNumber != -1){
                    favorite = new FavoriteItem(favorites.getString(favorites.getColumnIndex("AIRLINE")),
                            "Flight", String.valueOf(flightNumber));
                }
                else{
                    favorite = new FavoriteItem(favorites.getString(favorites.getColumnIndex("DEPARTURE_AIRPORT")),
                            "To", favorites.getString(favorites.getColumnIndex("ARRIVAL_AIRPORT")));
                }
                favoritesArray[i] = favorite;
                favorites.moveToNext();
            }
        }
        else{
            favoritesArray = new FavoriteItem[0];
        }
        favoriteAdapter = new FavoriteListAdapter(getContext(), R.layout.favorite_item, favoritesArray, getFragmentManager());
    }

    private void AddFavoriteTrip()
    {
        Intent i = new Intent(getContext(), AddFavoriteActivity.class);
        startActivity(i);
    }
}

