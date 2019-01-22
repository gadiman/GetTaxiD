package com.chagay.gettexid.controller;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.chagay.gettexid.R;
import com.chagay.gettexid.model.datasource.FireBase_DBDriver;
import com.chagay.gettexid.model.entities.Travel;
import com.google.firebase.database.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FirebaseBackgroundService extends Service {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference f = database.getReference("Travels");
    private ValueEventListener handler;
    private  ChildEventListener TravelRefChildEventListener;


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        TravelRefChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Travel travel = dataSnapshot.getValue(Travel.class);

                postNotif(travel.getDestinetionCityName());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        f.addChildEventListener(TravelRefChildEventListener);

    }

    private void postNotif(String notifString) {

        NotificationCompat.Builder notification = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setContentTitle("A new travel jest added")
                .setContentText("New travel at" + notifString)
                .setSmallIcon(R.drawable.ic_taxi);
        Intent intent_ = new Intent(this, MainActivityNavigation.class);
        PendingIntent pIntent_ = PendingIntent.getActivity(this,0, intent_, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pIntent_);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0,notification.build());





    }

}
