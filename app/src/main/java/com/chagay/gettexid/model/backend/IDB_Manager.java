package com.chagay.gettexid.model.backend;

import android.location.Location;
import com.chagay.gettexid.model.entities.Driver;
import com.chagay.gettexid.model.entities.Travel;
import java.util.Date;
import java.util.List;

public interface IDB_Manager {
    public List<Driver> GetNamesTheDrivers();
    public void AddDriver(Driver driver);
    public List<Travel> UntreatedTravels();
    public List<Travel> EndedTravels();
    public List<Travel> TravelByDriver(Driver driver);
    public List<Travel> UntreatedTravelsInDestinationCity(String city);
    public List<Travel> UntreatedTravelsByDistance(double distance, Location location);
    public List<Travel> TravelByDate(Date date);
    public List<Travel> TravelByPayment(double payment);
}
