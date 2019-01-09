package com.chagay.gettexid.model.datasource;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chagay.gettexid.model.backend.DB_Manager;
import com.chagay.gettexid.model.entities.Driver;
import com.chagay.gettexid.model.entities.Travel;
import com.google.firebase.database.*;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FireBase_DBDriver implements DB_Manager{

    public interface NotifyDataChange<T> {
        void onDataChanged(T obj);
        void onFailure(Exception exception);
    }

    static DatabaseReference TravelRef;
    static  DatabaseReference DriverRef;
    static List<Travel> TravelList;
    static List<Driver> DriverList;
    static {
          FirebaseDatabase database = FirebaseDatabase.getInstance();
          DriverRef = database.getReference( "Drivers" );
          TravelRef = database.getReference("Travels");
          TravelList = new ArrayList<>();
          DriverList = new ArrayList<>();
    }

    private static ChildEventListener TravelRefChildEventListener;
    private static ChildEventListener DriverRefChildEventListener;


    public static void NotifyToTravelsList(final NotifyDataChange<List<Travel>> notifyDataChange){

        if(notifyDataChange != null) {

            if (TravelRefChildEventListener != null) {
                notifyDataChange.onFailure(new Exception("first unNotify Travel list"));
                return;
            }

            TravelRefChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot,  String s) {
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
                public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    notifyDataChange.onFailure(databaseError.toException());
                }

            };
            TravelRef.addChildEventListener(TravelRefChildEventListener);
        }
    }

    public static void stopNotifyToTravelsList(){
        if (TravelRefChildEventListener != null) {
            TravelRef.removeEventListener(TravelRefChildEventListener);
            TravelRefChildEventListener = null;
        }
    }



    public static void NotifyToDriversList(final NotifyDataChange<List<Driver>> notifyDataChange){
        DriverRefChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot,  String s) {
                Driver driver = dataSnapshot.getValue(Driver.class);
                String id = dataSnapshot.getKey();
                driver.setId(Long.parseLong(id));
                DriverList.add(driver);
                notifyDataChange.onDataChanged(DriverList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot,String s) {
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
                    if (DriverList.get(i).getId()== id) {
                        DriverList.remove(i);
                        break;
                    }
                }
                notifyDataChange.onDataChanged(DriverList);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

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
    public List<Driver> getTheNamesOfDrivers() {
    return null;
    }


    @Override
    public boolean checkIfDriverAdded(String id) {
        return id == FirebaseDatabase.getInstance().getReference("Drivers").child(id).getKey();
    }

    @Override
    public String addDriver(Driver driver) {
        //create a new key for this driver
        DatabaseReference DriverRef = FireBase_DBDriver.DriverRef.push();
        //Insert the object to the FireBase
        DriverRef.setValue(driver);
        //return the key for checking if the insert succeeded
        return DriverRef.push().getKey();
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
