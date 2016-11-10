package com.example.brandon.list;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Brandon on 11/7/2016.
 */

public class AddFavoriteActivity extends AppCompatActivity {
    private TextView addFavoriteFirstTextView, addFavoriteSecondTextView, addFavoriteErrorTextView;
    private EditText addFavoriteFirstEditText, addFavoriteSecondEditText;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_favorite);

        db = new DatabaseHelper(this);

        addFavoriteFirstTextView = (TextView)findViewById(R.id.addFavoriteFirstTextView);
        addFavoriteSecondTextView = (TextView)findViewById(R.id.addFavoriteSecondTextView);
        addFavoriteFirstEditText = (EditText)findViewById(R.id.addFavoriteFirstEditText);
        addFavoriteSecondEditText = (EditText)findViewById(R.id.addFavoriteSecondEditText);
        addFavoriteErrorTextView = (TextView)findViewById(R.id.addFavoriteErrorTextView);
        addFavoriteErrorTextView.setTextColor(Color.RED);
        addFavoriteFirstTextView.setText("Origin : ");
        addFavoriteSecondTextView.setText("Destination : ");
    }

    public void SaveFavorite(View view){
        String firstEditText = String.valueOf(addFavoriteFirstEditText.getText());
        String secondEditText = String.valueOf(addFavoriteSecondEditText.getText());
        boolean success;
        if(firstEditText.equalsIgnoreCase("") || secondEditText.equalsIgnoreCase("")){
            addFavoriteErrorTextView.setText("Please fill in all fields");
            success = false;
        }
        else {
            success = db.InsertFavoritesByDestination(firstEditText, secondEditText);
        }
        if(success){
            finish();
        }
        else{
            addFavoriteErrorTextView.setText("Save failed");
        }
    }
}
