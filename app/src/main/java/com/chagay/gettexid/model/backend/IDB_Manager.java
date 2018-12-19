package com.chagay.gettexid.model.backend;

import android.location.Location;
import com.chagay.gettexid.model.entities.Driver;
import com.chagay.gettexid.model.entities.Travel;
import java.util.Date;
import java.util.List;

public interface IDB_Manager {
    public List<Driver> getTheNamesOfDrivers();
    public void addDriver(Driver driver);
    public List<Travel> untreatedTravels();
    public List<Travel> endedTravels();
    public List<Travel> travelsByDriver(Driver driver);
    public List<Travel> untreatedTravelsInDestinationCity(String city);
    public List<Travel> untreatedTravelsByDistance(double distance, Location location);
    public List<Travel> travelsByDate(Date date);
    public List<Travel> travelsByPayment(double payment);
}
