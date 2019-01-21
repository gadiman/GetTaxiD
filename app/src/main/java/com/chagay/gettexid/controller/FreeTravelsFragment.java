package com.chagay.gettexid.controller;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import com.chagay.gettexid.R;
import android.app.Fragment;

import com.chagay.gettexid.model.backend.DB_Manager;
import com.chagay.gettexid.model.backend.FactoryMethod;
import com.chagay.gettexid.model.datasource.FireBase_DBDriver;
import com.chagay.gettexid.model.entities.Travel;

import java.util.ArrayList;
import java.util.List;

public  class FreeTravelsFragment extends Fragment {

    ListView listViewTravels;
    View contact_fragment;
    List<Travel> Travels;
    TravelsListViewAdapter travelsListViewAdapter;
    static boolean flag = false;
    DB_Manager db_manager = FactoryMethod.getManager() ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contact_fragment = inflater.inflate(R.layout.fragment_1, container, false);
        listViewTravels = (ListView) contact_fragment.findViewById(R.id.listViewTravels);


            Travels = new ArrayList<>();

            FireBase_DBDriver.NotifyToTravelsList(new FireBase_DBDriver.NotifyDataChange<List<Travel>>() {
                @Override
                public void onDataChanged(List<Travel> obj) {

                    if (travelsListViewAdapter == null) {
                        Travels = db_manager.untreatedTravels();
                        travelsListViewAdapter = new TravelsListViewAdapter(getActivity(), R.layout.list_item, Travels);
                        listViewTravels.setAdapter(travelsListViewAdapter);
                        flag = true;
                    } else {
                        Travels = db_manager.untreatedTravels();
                        travelsListViewAdapter.notifyDataSetChanged();
                    }
                }


                @Override
                public void onFailure(List<Travel> obj) {
                   Travels = db_manager.untreatedTravels();
                    if (travelsListViewAdapter == null)
                        travelsListViewAdapter = new TravelsListViewAdapter(getActivity(), R.layout.list_item, Travels);
                   travelsListViewAdapter.notifyDataSetChanged();
                   listViewTravels.setAdapter(travelsListViewAdapter);
                }
            });


        registerForContextMenu(listViewTravels);
        return contact_fragment;
    }



}










