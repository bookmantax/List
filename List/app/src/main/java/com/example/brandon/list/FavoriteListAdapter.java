package com.example.brandon.list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Brandon on 11/8/2016.
 */

public class FavoriteListAdapter extends ArrayAdapter<FavoriteItem> {

    FavoriteHolder holder;
    Context context;
    int layoutResourceId;
    FragmentManager fragmentManager;
    FavoriteItem data[] = null;
    FavoriteItem fav;

    public FavoriteListAdapter(Context context, int layoutResourceId, FavoriteItem[] data, FragmentManager fragmentManager)
    {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent)
    {
        View row = convertView;
        holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new FavoriteHolder();
            holder.txtFirstTextView = (TextView)row.findViewById(R.id.favoriteItemFirstTextView);
            holder.txtSecondTextView = (TextView)row.findViewById(R.id.favoriteItemSecondTextView);
            holder.txtThirdTextView = (TextView)row.findViewById(R.id.favoriteItemThirdTextView);

            row.setTag(holder);
        }
        else
        {
            holder = (FavoriteHolder)row.getTag();
        }

        fav = data[position];
        holder.txtFirstTextView.setText(fav.airlineOrOrigin);
        holder.txtSecondTextView.setText(fav.toOrFlight);
        holder.txtThirdTextView.setText(fav.flightNumberOrDestination);
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).isFavoritesSearch = true;
                ((MainActivity)context).airlineOrOrigin = fav.airlineOrOrigin;
                ((MainActivity)context).flightNumberOrDestination = fav.flightNumberOrDestination;
                Fragment results = new ResultsFragment();
                fragmentManager.beginTransaction().replace(R.id.mainLayout, results).commit();
            }
        });

        return row;
    }

    static class FavoriteHolder
    {
        TextView txtFirstTextView;
        TextView txtSecondTextView;
        TextView txtThirdTextView;
    }
}
