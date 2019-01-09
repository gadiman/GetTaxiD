package com.chagay.gettexid.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chagay.gettexid.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nameEditText;
    private EditText passEditText;
    private Button loginButton;
    private Button signUpButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        findViews();
    }

    private void findViews() {
        loginButton = (Button) findViewById( R.id.loginButton );
        signUpButton = (Button) findViewById( R.id.signUpButton );
        nameEditText = (EditText) findViewById( R.id.nameEditText );
        passEditText = (EditText) findViewById( R.id.passEditText );
        loginButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }


    public void onClick(View v) {
        if (v == loginButton);
            //saveSharedPreferences( nameEditText.text, passEditText.text );
        else if (v == signUpButton) {
            Intent registering = new Intent( LoginActivity.this, RegisterActivity.class );
            startActivity(registering);
        }
    }


    private void saveSharedPreferences(String name, String pass) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences( this );
        if (sharedPreferences.contains( "NAME" )) {
            nameEditText.setText( sharedPreferences.getString( "NAME", null ) );
            Toast.makeText( this, "load name", Toast.LENGTH_SHORT ).show();
        }

        if (sharedPreferences.contains( "PASSWORD" )) {
            int age = sharedPreferences.getInt( "PASSWORD", 0 );
            passEditText.setText( String.valueOf( age ) );
            Toast.makeText( this, "load pass", Toast.LENGTH_SHORT ).show();
        }


    }
}

