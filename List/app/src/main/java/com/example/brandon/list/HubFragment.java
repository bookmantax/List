package com.example.brandon.list;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by Brandon on 10/18/2016.
 */

public class HubFragment extends android.support.v4.app.Fragment
{
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private Button favoritesAddFavoriteButton;
    private ListView favoritesResultsListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = getActivity().getSharedPreferences("UserPreferences", 0);
        editor = settings.edit();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        favoritesAddFavoriteButton = (Button)rootView.findViewById(R.id.favoritesAddFavoriteButton);
        favoritesAddFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFavoriteTrip();
            }
        });

        favoritesResultsListView = (ListView)rootView.findViewById(R.id.favoritesResultsListView);

        return rootView;
    }

    private void AddFavoriteTrip()
    {

    }
}
