package com.chagay.gettexid.controller;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chagay.gettexid.R;
import com.chagay.gettexid.model.backend.FactoryMethod;
import com.chagay.gettexid.model.entities.Driver;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText driverID;
    private EditText driverFirstName;
    private EditText driverLastName;
    private EditText driverUserName;
    private EditText password;
    private EditText driverPhoneNumber;
    private EditText driverEmailAddress;
    private EditText creditCard;
    private Button submitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );
        findViews();
        createOnChangeFocuseListeners();
    }


    private void findViews() {
        driverID = (EditText) findViewById( R.id.IDEditText );
        driverFirstName = (EditText) findViewById( R.id.firstNameEditText );
        driverLastName = (EditText) findViewById( R.id.lastNameEditText );
        driverUserName = (EditText) findViewById( R.id.userNameEditText );
        password = (EditText) findViewById( R.id.PasswordEditText );
        driverPhoneNumber = (EditText) findViewById( R.id.PhoneEditText );
        driverEmailAddress = (EditText) findViewById( R.id.emailaddressEditText );
        creditCard = (EditText) findViewById( R.id.creditCardEditText );
        submitButton = (Button) findViewById( R.id.submitButton );
        submitButton.setOnClickListener(this);
    }

    private void createOnChangeFocuseListeners() {
        driverEmailAddress.setOnFocusChangeListener( new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher( driverEmailAddress.getText().toString() ).matches()) {
                    driverEmailAddress.setError( "not valid email" );

                }
            }
        } );

        driverFirstName.setOnFocusChangeListener( new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!driverFirstName.getText().toString().matches( "[a-zA-Z\u0590-\u05fe]+" )) {
                    driverFirstName.setError( "not valid first name" );
                }
            }
        } );

        driverLastName.setOnFocusChangeListener( new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!driverLastName.getText().toString().matches( "[a-zA-Z\u0590-\u05fe]+" )) {
                    driverLastName.setError( "not valid last name" );
                }
            }
        } );

        driverPhoneNumber.setOnFocusChangeListener( new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (driverPhoneNumber.getText().toString().length() != 10)
                    driverPhoneNumber.setError( "not valid phone number" );
            }
        } );

        creditCard.setOnFocusChangeListener( new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (creditCard.getText().toString().length() != 16)
                    creditCard.setError( "not valid credit card" );
            }
        } );

        driverID.setOnFocusChangeListener( new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (driverID.getText().toString().length() != 9)
                    driverID.setError( "not valid ID" );
            }
        } );
    }

    @SuppressLint("StaticFieldLeak")
    public void onClick(View v) {
        if (v == submitButton) {
            if (!ValidateSubmitButton()) {
                Toast.makeText( this, "Fill out all fields before submitting", Toast.LENGTH_SHORT ).show();
                return;
            }
            else {

                String id = driverID.getText().toString();
                String firstName = driverFirstName.getText().toString();
                String lastName = driverLastName.getText().toString();
                String userName = driverUserName.getText().toString();
                String _password = password.getText().toString();
                String numberPhone = driverPhoneNumber.getText().toString();
                String emailAddress = driverEmailAddress.getText().toString();
                String _creditCard = creditCard.getText().toString();

                final Driver driver = new Driver( id, firstName, lastName, userName, _password, numberPhone,
                        emailAddress, _creditCard );
                new AsyncTask<Void, Void, Boolean>(){

                    @Override
                    protected Boolean doInBackground(Void... voids) {
                        String id= FactoryMethod.getManager().addDriver(driver);
                        return FactoryMethod.getManager().checkIfDriverAdded(id);
                    }
                    protected void onPostExecute(Boolean aBoolean) {
                        super.onPostExecute(aBoolean);
                        if (aBoolean)
                            Toast.makeText(RegisterActivity.this, "Add to database successful", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(RegisterActivity.this, "Add to database not successful", Toast.LENGTH_SHORT).show();
                    }
                }.execute();
            }
        }
    }


    private boolean ValidateSubmitButton() {
        if (driverID.getText().toString().isEmpty() || driverFirstName.getText().toString().isEmpty() ||
                driverLastName.getText().toString().isEmpty() || driverUserName.getText().toString().isEmpty() ||
                password.getText().toString().isEmpty() || driverPhoneNumber.getText().toString().isEmpty() ||
                driverEmailAddress.getText().toString().isEmpty() || creditCard.getText().toString().isEmpty())
            return false;
        return true;
    }

}
