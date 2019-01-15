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
import com.chagay.gettexid.model.backend.FactoryMethod;
import com.chagay.gettexid.model.entities.Driver;

import java.util.List;

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
        loginButton.setOnClickListener( this );
        signUpButton.setOnClickListener( this );
    }


    public void onClick(View v) {
        if (v == loginButton) {
            String user = nameEditText.getText().toString();
            String password = passEditText.getText().toString();
            if (!ValidateloginButton()){
                Toast.makeText( this, "Fill out all fields before submitting", Toast.LENGTH_SHORT ).show();
                return;
            }
            if (!isDriverExists(user,password)){
                Toast.makeText( this, "The Username or password is incorrect", Toast.LENGTH_SHORT ).show();
                return;
            }
            else {
                //saveSharedPreferences??????????
                Intent mainIntent=new Intent( this,MainActivity.class );
                finish();
                startActivity( mainIntent );
            }
        } else if (v == signUpButton) {
            Intent registering = new Intent( this, RegisterActivity.class );
            finish();
            startActivity( registering );
        }
    }

    private boolean ValidateloginButton() {
        if (nameEditText.getText().toString().isEmpty() || passEditText.getText().toString().isEmpty())
            return false;
        return true;
    }


    private boolean isDriverExists(String user, String pass){
        List<Driver> drivers= FactoryMethod.getManager().getAllTheDrivers();
        for (Driver it:drivers) {
            if(it.getDriverUserName() == user&&it.getPassword()==pass){
                return true;
            }
        }
        return false;
    }

/*
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


    }*/
}

