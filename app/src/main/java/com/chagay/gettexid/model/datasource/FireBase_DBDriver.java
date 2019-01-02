package com.chagay.gettexid.model.datasource;

import android.location.Location;

import com.chagay.gettexid.model.backend.DB_Manager;
import com.chagay.gettexid.model.entities.Driver;
import com.chagay.gettexid.model.entities.Travel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.List;

public class FireBase_DBDriver implements DB_Manager{
    @Override
    public List<Driver> getTheNamesOfDrivers() {
        return null;
    }

    @Override
    public String addDriver(Driver driver) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference firebaseRef = database.getReference( "Drivers" );
        //create a new key for this driver
        DatabaseReference DriverRef = firebaseRef.push();
        //Insert the object to the FireBase
        DriverRef.setValue(driver);
        //return the key for checking if the insert succeeded
        return DriverRef.push().getKey();
    }

    @Override
    public List<Travel> untreatedTravels() {
        return null;
    }

    @Override
    public List<Travel> endedTravels() {
        return null;
    }

    @Override
    public List<Travel> travelsByDriver(Driver driver) {
        return null;
    }

    @Override
    public List<Travel> untreatedTravelsInDestinationCity(String city) {
        return null;
    }

    @Override
    public List<Travel> untreatedTravelsByDistance(double distance, Location location) {
        return null;
    }

    @Override
    public List<Travel> travelsByDate(Date date) {
        return null;
    }

    @Override
    public List<Travel> travelsByPayment(double payment) {
        return null;
    }
}
