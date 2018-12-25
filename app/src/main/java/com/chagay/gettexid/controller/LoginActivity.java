package com.chagay.gettexid.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.chagay.gettexid.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );


    }

    public void onClick(View v) {
        if (v.getId() == loginButton)
            saveSharedPreferences();
        else if (v.getId() == signUpButton) {
            final Intent nextIntent = new Intent( LoginActivity.this, RegisterActivity.class );
        }
    }


    private void saveSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences( this );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String name = nameEditText.getText().toString();
        String pass = passEditText.getText().toString();
        editor.putString("NAME", name);
        editor.putString("PASSWORD", pass);
        editor.commit();
    }
}

