package com.chagay.gettexid.model.datasource;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;

import com.chagay.gettexid.model.backend.DB_Manager;
import com.chagay.gettexid.model.entities.Driver;
import com.chagay.gettexid.model.entities.Travel;
import com.google.firebase.database.*;
import com.google.firebase.database.FirebaseDatabase;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class  FireBase_DBDriver extends Activity implements DB_Manager  {

    public interface NotifyDataChange<T> {
        void onDataChanged(T obj);

        void onFailure(Exception exception);
    }


    static DatabaseReference TravelRef;
    static DatabaseReference DriverRef;
    static List<Travel> TravelList;
    static List<Driver> DriverList;

    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DriverRef = database.getReference("Drivers");
        TravelRef = database.getReference("Travels");
        TravelList = new ArrayList<>();
        DriverList = new ArrayList<>();

    }

    private static ChildEventListener TravelRefChildEventListener;
    private static ChildEventListener DriverRefChildEventListener;


    public static void NotifyToTravelsList(final NotifyDataChange<List<Travel>> notifyDataChange) {

        if (notifyDataChange != null) {

            if (TravelRefChildEventListener != null) {
                notifyDataChange.onFailure(new Exception("first unNotify Travel list"));
                return;
            }

            TravelRefChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                    Travel travel = dataSnapshot.getValue(Travel.class);
                    String id = dataSnapshot.getKey();
                    travel.setId(Long.parseLong(id));
                    TravelList.add(travel);
                    notifyDataChange.onDataChanged(TravelList);

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    Travel travel = dataSnapshot.getValue(Travel.class);
                    Long id = Long.parseLong(dataSnapshot.getKey());
                    travel.setId(id);
                    for (int i = 0; i < TravelList.size(); i++) {
                        if (TravelList.get(i).getId().equals(id)) {
                            TravelList.set(i, travel);
                            break;
                        }
                        notifyDataChange.onDataChanged(TravelList);
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Travel travel = dataSnapshot.getValue(Travel.class);
                    Long id = Long.parseLong(dataSnapshot.getKey());
                    travel.setId(id);
                    for (int i = 0; i < TravelList.size(); i++) {
                        if (TravelList.get(i).getId() == id) {
                            TravelList.remove(i);
                            break;
                        }
                    }
                    notifyDataChange.onDataChanged(TravelList);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    notifyDataChange.onFailure(databaseError.toException());
                }

            };
            TravelRef.addChildEventListener(TravelRefChildEventListener);
        }
    }

    public static void stopNotifyToTravelsList() {
        if (TravelRefChildEventListener != null) {
            TravelRef.removeEventListener(TravelRefChildEventListener);
            TravelRefChildEventListener = null;
        }
    }


    public static void NotifyToDriversList(final NotifyDataChange<List<Driver>> notifyDataChange) {
        DriverRefChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Driver driver = dataSnapshot.getValue(Driver.class);
                String id = dataSnapshot.getKey();
                driver.setId(Long.parseLong(id));
                DriverList.add(driver);
                notifyDataChange.onDataChanged(DriverList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Driver driver = dataSnapshot.getValue(Driver.class);
                Long id = Long.parseLong(dataSnapshot.getKey());
                for (int i = 0; i < DriverList.size(); i++) {
                    if (DriverList.get(i).getId().equals(id)) {
                        DriverList.set(i, driver);
                        break;
                    }
                }
                notifyDataChange.onDataChanged(DriverList);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Driver driver = dataSnapshot.getValue(Driver.class);
                Long id = Long.parseLong(dataSnapshot.getKey());
                for (int i = 0; i < DriverList.size(); i++) {
                    if (DriverList.get(i).getId() == id) {
                        DriverList.remove(i);
                        break;
                    }
                }
                notifyDataChange.onDataChanged(DriverList);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                notifyDataChange.onFailure(databaseError.toException());
            }
        };
        DriverRef.addChildEventListener(TravelRefChildEventListener);
    }

    public static void stopNotifyToDriversList() {
        if (DriverRefChildEventListener != null) {
            DriverRef.removeEventListener(DriverRefChildEventListener);
            DriverRefChildEventListener = null;
        }
    }

    @Override
    public List<String> getTheNamesOfDrivers() {
        List<String>namesOfDrivers = new ArrayList<>();
        for (Driver it:DriverList) {
            namesOfDrivers.add(it.getDriverFirstName());
        }
        return namesOfDrivers;
    }

    @Override
    public List<Driver> getAllTheDrivers() {
        return DriverList;
    }


    @Override
    public boolean checkIfDriverAdded(String id) {
        return id == FirebaseDatabase.getInstance().getReference("Drivers").child(id).getKey();
    }

    @Override
    public String addDriver(Driver driver) {
        //create a key for this driver based on DriverID
        DatabaseReference DriverRef = FireBase_DBDriver.DriverRef.child(driver.getDriverID());
        //Insert the object to the FireBase

        DriverRef.setValue(driver);
        return DriverRef.child(driver.getDriverID()).getKey();
     }

    @Override
    public List<Travel> untreatedTravels() {
        if (TravelList == null)
            return null;

        List<Travel> untreatedTravelList = new ArrayList<>();
        for (Travel it : TravelList) {
            if (it.getTravel_status() == Travel.TRAVEL_STATUS.AVAILABLE) {
                untreatedTravelList.add(it);
            }
        }
        return untreatedTravelList;
    }


    @Override
    public List<Travel> endedTravels() {
        if (TravelList == null)
            return null;

        List<Travel> untreatedTravelList = new ArrayList<>();
        for (Travel it : TravelList) {
            if (it.getTravel_status() == Travel.TRAVEL_STATUS.FINISHED) {
                untreatedTravelList.add(it);
            }
        }
        return untreatedTravelList;
    }

    @Override
    public List<Travel> travelsByDriver(Driver driver) {
        List<Travel>travels = new ArrayList<>();
        for (Travel it:TravelList) {
            if(it.getDriverID() == driver.getDriverID()){
                travels.add(it);
            }
        }
        return travels;
    }

    @Override
    public List<Travel> untreatedTravelsInDestinationCity(String city) {
        List<Travel> travels = new ArrayList<>();
        for (Travel it:TravelList) {
            if(it.getTravel_status() == Travel.TRAVEL_STATUS.AVAILABLE &&
                    it.getDestinetionCityName() == city){
                travels.add(it);
            }

        }
        return travels;
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

    public  void castStringToLocations(Travel travel){
        Geocoder geoCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try
        {
            List<Address> addresses = geoCoder.getFromLocationName(travel.getStartLocation(), 5);
            if (addresses != null && addresses.size() > 0)
            {
                Double lat = (double) (addresses.get(0).getLatitude());
                Double lon = (double) (addresses.get(0).getLongitude());

                Address address = addresses.get(0);
                String city = address.getLocality();
                travel.setIntialLocationLatitude(lat);
                travel.setInitialLocationlongitude(lon);
                travel.setDestinetionCityName(city);

            }
            List<Address> addresses_ = geoCoder.getFromLocationName(travel.getEndLocation(), 5);
            if (addresses != null && addresses.size() > 0)
            {
                Double lat = (double) (addresses.get(0).getLatitude());
                Double lon = (double) (addresses.get(0).getLongitude());

                travel.setDestinetionLatitude(lat);
                travel.setDestinetionlongitude(lon);

            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
