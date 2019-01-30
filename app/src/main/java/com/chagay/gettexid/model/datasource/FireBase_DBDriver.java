package com.chagay.gettexid.model.datasource;

import android.app.Activity;
import android.location.Location;
import android.support.annotation.NonNull;
import com.chagay.gettexid.model.backend.DB_Manager;
import com.chagay.gettexid.model.entities.Driver;
import com.chagay.gettexid.model.entities.Travel;
import com.google.firebase.database.*;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class  FireBase_DBDriver extends Activity implements DB_Manager  {

    public FireBase_DBDriver(){
        NotifyToDriversList(new NotifyDataChange<List<Driver>>() {
            @Override
            public void onDataChanged(List<Driver> obj) {

            }

            @Override
            public void onFailure(List<Driver> obj) {

            }
        });

        NotifyToTravelsList(new NotifyDataChange<List<Travel>>() {
            @Override
            public void onDataChanged(List<Travel> obj) {

            }

            @Override
            public void onFailure(List<Travel> obj) {

            }
        });

    }





    public interface NotifyDataChange<T> {
        void onDataChanged(T obj);
        void onFailure(T obj );
    }
    final int Pay =5;
    static DatabaseReference TravelRef;
    static DatabaseReference DriverRef;
    static List<Travel> TravelList;
    static List<Travel> FreeTravelList;
    static List<Travel> FinishTravelList;
    static List<Driver> DriverList;
    static Driver currentDriver;


    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DriverRef = database.getReference("Drivers");
        TravelRef = database.getReference("Travels");
        TravelList = new ArrayList<>();
        DriverList = new ArrayList<>();
        FreeTravelList = new ArrayList<>();


    }

    private static ChildEventListener TravelRefChildEventListener;
    private static ChildEventListener DriverRefChildEventListener;


    public static void NotifyToTravelsList(final NotifyDataChange<List<Travel>> notifyDataChange) {
        if (notifyDataChange != null) {

            if (TravelRefChildEventListener != null) {
                notifyDataChange.onFailure(TravelList);
                return;
            }
            TravelRefChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                    Travel travel = dataSnapshot.getValue(Travel.class);
                    String id = dataSnapshot.getKey();
                    travel.setId(id);

                    TravelList.add(travel);
                    notifyDataChange.onDataChanged(TravelList);

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    Travel travel = dataSnapshot.getValue(Travel.class);
                    String id = dataSnapshot.getKey();
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
                    String id = dataSnapshot.getKey();
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
                    notifyDataChange.onFailure(null);
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
                notifyDataChange.onFailure(null);
            }
        };
            DriverRef.addChildEventListener(DriverRefChildEventListener);
    }

    public static void stopNotifyToDriversList() {
        if (DriverRefChildEventListener != null) {
            DriverRef.removeEventListener(DriverRefChildEventListener);
            DriverRefChildEventListener = null;
        }
    }


    public static void NotifyCurrentDriver(String id)
    {
        for (Driver it:DriverList) {
            if(id == it.getDriverID())
                currentDriver = it;
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
    public Driver getCurrentDriver() {
        return currentDriver;
    }

    @Override
    public String updateTravelDetails(String key, String driverId) {

       TravelRef.child(key).child("driverID").setValue(driverId);
       TravelRef.child(key).child("travel_status").setValue(Travel.TRAVEL_STATUS.OCCUPIED);

        return TravelRef.child(key).child(driverId).getKey();
    }

    @Override
    public String updateTravelFinish(String key) {
        TravelRef.child(key).child("travel_status").setValue(Travel.TRAVEL_STATUS.FINISHED);
        return TravelRef.child(key).child("travel_status").getKey();
    }

    @Override
    public void resetAll() {
        stopNotifyToTravelsList();
        stopNotifyToDriversList();
        TravelList.clear();
        DriverList.clear();
    }

    @Override
    public Boolean thereIsPreviousTrip(String id) {
        for (Travel it:TravelList) {
            if(it.getDriverID()!= null && it.getDriverID().equals(id) &&
                    it.getTravel_status() == Travel.TRAVEL_STATUS.OCCUPIED)
                return true;
        }
            return false;
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
    public List<Travel> untreatedTravels(String id) {
        if (TravelList == null)
            return null;

        FreeTravelList.clear();
        for (Travel it : TravelList) {
            if (it.getTravel_status() == Travel.TRAVEL_STATUS.AVAILABLE ||it.getId().equals(id)) {
                FreeTravelList.add(it);
            }
        }

        return FreeTravelList;
    }

    @Override
    public List<Travel> finishedTravels(String id){
        if (TravelList==null)
            return null;

        for (Travel it:TravelList){
            if (it.getDriverID() != null && it.getDriverID().equals(id))
                FreeTravelList.add( it );
        }
        return FreeTravelList;

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
    public List<Travel> untreatedTravelsByDistance(double distance) {
        List<Travel> tmp = new ArrayList<>();
        for (Travel it:FreeTravelList) {
            if (calculateDistance(it) <= distance)
                tmp.add(it);
        }
        return tmp;
    }

    @Override
    public List<Travel> travelsByDate(Date date) {

        List<Travel> tmp = new ArrayList<>();
        for (Travel it:TravelList) {
            if (new SimpleDateFormat("dd-mm-yyyy", Locale.getDefault()).format(date).equals(it.getDateOfTravel()))
                tmp.add(it);
        }

        return tmp;
    }

    @Override
    public List<Travel> travelsByPayment(double payment) {
        List<Travel> tmp = new ArrayList<>();
        for (Travel it:FreeTravelList) {
            if (calculateDistance(it)* Pay <= payment )
                tmp.add(it);
        }
        return tmp;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FireBase_DBDriver.stopNotifyToTravelsList();
    }



    private  int calculateDistance(Travel travel) {

        Driver driver = getCurrentDriver();
        double a = driver.getLongitude();
        double b = driver.getLatitude();


        Location locationA = new Location("point A");
        locationA.setLatitude(b);
        locationA.setLongitude(a);

        Location locationB = new Location("point B");
        double a_ = travel.getInitialLocationLongitude();
        double b_ = travel.getIntialLocationLatitude();

        locationB.setLatitude(b_);
        locationB.setLongitude(a_);



        return Math.round(locationA.distanceTo(locationB)/1000);
    }


}
