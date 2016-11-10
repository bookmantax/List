package com.example.brandon.list;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Brandon on 10/18/2016.
 */

public class SignupActivity extends AppCompatActivity
{
    String firstName, lastName, emailAddress;
    private ProgressBar signupProgressBar;
    EditText firstEditText, lastEditText, emailEditText;
    TextView signupResponseTextView;
    Button createUserButton;
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupResponseTextView = (TextView)findViewById(R.id.signupResponseTextView);
        firstEditText = (EditText)findViewById(R.id.firstEditText);
        lastEditText = (EditText)findViewById(R.id.lastEditText);
        emailEditText = (EditText)findViewById(R.id.emailEditText);
        createUserButton = (Button)findViewById(R.id.createAccountButton);
        signupProgressBar = (ProgressBar)findViewById(R.id.signupProgressBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void CreateUser(View view)
    {
        createUserButton.setEnabled(false);
        signupProgressBar.setVisibility(View.VISIBLE);

        firstName = String.valueOf(firstEditText.getText());
        lastName = String.valueOf(lastEditText.getText());
        emailAddress = String.valueOf(emailEditText.getText());

        if(RequiredInformationProvided(firstName, lastName, emailAddress))
        {

        }
        else
        {
            signupResponseTextView.setText("Please make sure all fields have a value.");
            signupResponseTextView.setTextColor(Color.RED);
            createUserButton.setEnabled(true);
            signupProgressBar.setVisibility(View.GONE);
        }
    }

    private boolean RequiredInformationProvided(String firstName, String lastName, String emailAddress)
    {
        if(firstName != null && lastName != null && emailAddress != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}