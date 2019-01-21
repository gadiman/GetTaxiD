package com.chagay.gettexid.controller;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.chagay.gettexid.R;
import com.chagay.gettexid.model.datasource.FireBase_DBDriver;
import com.chagay.gettexid.model.entities.Driver;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import org.shredzone.commons.suncalc.SunTimes;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivityNavigation extends AppCompatActivity {


    private final static int PLACE_PICKER_RESULT = 1;

    //Those fields to manager the location
    // Acquire a reference to the system Location Manager
    LocationManager locationManager;
    // Define a listener that responds to location updates
    LocationListener locationListener;
    public Criteria criteria;
    public String bestProvider;
    Driver driver = new Driver();

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;

    private NavigationView nv;
    private static int fragment = 0;
    FreeTravelsFragment fragment1 = new FreeTravelsFragment();
    SelectedTravelsFragment fragment2 = new SelectedTravelsFragment();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);

        getLocation();

        dl = (DrawerLayout) findViewById(R.id.activity_main_);
        t = new ActionBarDrawerToggle(this, dl, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle("Close          ");
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle("Open          ");
                supportInvalidateOptionsMenu();
            }

        };


        dl.addDrawerListener(t);
        t.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nv = (NavigationView) findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_f1:
                        loadFragment(fragment1);
                        fragment = 1;
                        return true;
                    case R.id.nav_f2:
                        loadFragment(fragment2);
                        fragment = 2;
                        return true;
                    case R.id.nav_f3:
                        finish();
                        System.exit(0);
                    default:
                        return true;
                }
            }

        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (t.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);

    }


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onStart() {
        super.onStart();
        if (fragment == 1)
            loadFragment(fragment1);
        else if (fragment == 2)
            loadFragment(fragment2);


        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void loadFragment(Fragment fragment) {
        dl.closeDrawer(nv);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }

    //This function get the location of the user by default (when the activity is run at first)
    @SuppressLint("StaticFieldLeak")
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLocation() {

        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {

                if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    criteria = new Criteria();
                    bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true));

                    Location location = locationManager.getLastKnownLocation(bestProvider);
                    if (location != null) {
                        driver.setLatitude(location.getLatitude());
                        driver.setLongitude(location.getLongitude());
                        return true;
                    } else {
                        locationManager.requestLocationUpdates(bestProvider, 0, 0, locationListener);
                    }
                }
                else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean location) {
                super.onPostExecute(location);
                if(! location){
                    Toast.makeText(getApplicationContext(), "We didn't found you, please pick up your location", Toast.LENGTH_SHORT).show();
                    PickCurrentLocation();
                }

            }
        }.execute();


    }

    //This function for location permission ask
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            } else {
                Toast.makeText(this, "Until you grant the permission, we can not display the location", Toast.LENGTH_SHORT).show();
            }
        }

    }

    //This function update the current location for case that the user walking
    void showSunTimes(double lat, double lng) {
        Date date = new Date();// date of calculation

        SunTimes times = SunTimes.compute()
                .on(date)       // set a date
                .at(lat, lng)   // set a location
                .execute();     // get the results
        System.out.println("Sunrise: " + times.getRise());
        System.out.println("Sunset: " + times.getSet());
    }


    void PickCurrentLocation() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        Intent intent;

        try {
            startActivityForResult(builder.build(MainActivityNavigation.this), PLACE_PICKER_RESULT);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_RESULT) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String address = place.getAddress().toString();
                LatLng queriedLocation = place.getLatLng();
                driver.setLatitude(queriedLocation.latitude);
                driver.setLongitude(queriedLocation.longitude);

            }
        }


    }
}
