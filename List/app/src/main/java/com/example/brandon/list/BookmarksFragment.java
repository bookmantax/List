package com.example.brandon.list;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

/**
 * Created by Brandon on 10/18/2016.
 */

public class BookmarksFragment extends android.support.v4.app.Fragment
{
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private ExpandableListView bookmarksExpandableListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        bookmarksExpandableListView = (ExpandableListView)rootView.findViewById(R.id.bookmarksFlightsExpandableListView);

        return rootView;
    }
}
