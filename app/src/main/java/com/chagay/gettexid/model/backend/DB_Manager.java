package com.chagay.gettexid.model.backend;

import android.content.ContentValues;
import android.location.Location;
import com.chagay.gettexid.model.entities.Driver;
import com.chagay.gettexid.model.entities.Travel;
import java.util.Date;
import java.util.List;

public interface DB_Manager {
    public List<String> getTheNamesOfDrivers();
    public List<Driver> getAllTheDrivers();
    public String addDriver(Driver driver);
    public boolean checkIfDriverAdded(String id);

    public List<Travel>  untreatedTravels();
    public List<Travel> endedTravels();
    public List<Travel> travelsByDriver(Driver driver);
    public List<Travel> untreatedTravelsInDestinationCity(String city);
    public List<Travel> untreatedTravelsByDistance(double distance, Location location);
    public List<Travel> travelsByDate(Date date);
    public List<Travel> travelsByPayment(double payment);
}
