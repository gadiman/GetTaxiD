package com.chagay.gettexid.controller;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.chagay.gettexid.R;
import com.chagay.gettexid.model.backend.DB_Manager;
import com.chagay.gettexid.model.backend.FactoryMethod;
import com.chagay.gettexid.model.datasource.FireBase_DBDriver;
import com.chagay.gettexid.model.entities.Travel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectedTravelsFragment extends Fragment {

    View contact_fragment;
    ListView listViewTravels;
    List<Travel> FinishedTravels;
    DB_Manager db_manager = FactoryMethod.getManager();
    FinishedTravelsAdapter finishedTravelsAdapter;
    String driverID = db_manager.getCurrentDriver().getDriverID();
    Travel travel;
    int PositionclickLst;
    EditText filterByDestination;



    public SelectedTravelsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contact_fragment = inflater.inflate( R.layout.fragment_2, container, false );
        listViewTravels = (ListView) contact_fragment.findViewById( R.id.listViewFinishedTravels );
        FinishedTravels = new ArrayList<>();

        FireBase_DBDriver.NotifyToTravelsList( new FireBase_DBDriver.NotifyDataChange<List<Travel>>() {


            @Override
            public void onDataChanged(List<Travel> obj) {
                if (finishedTravelsAdapter == null) {
                    FinishedTravels = db_manager.finishedTravels( driverID );
                    finishedTravelsAdapter = new FinishedTravelsAdapter( getActivity(), R.layout.list_item, FinishedTravels );
                    listViewTravels.setAdapter( finishedTravelsAdapter );
                } else {
                    FinishedTravels = db_manager.finishedTravels( driverID );
                    finishedTravelsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(List<Travel> obj) {
                FinishedTravels = db_manager.finishedTravels( driverID );
                if (finishedTravelsAdapter == null)
                    finishedTravelsAdapter = new FinishedTravelsAdapter( getActivity(), R.layout.list_item, FinishedTravels );
                finishedTravelsAdapter.notifyDataSetChanged();
                listViewTravels.setAdapter( finishedTravelsAdapter );
            }
        } );
        findViews(contact_fragment);
        setListeners(contact_fragment);
        listViewTravels.setTextFilterEnabled(true);
        registerForContextMenu(listViewTravels);
        return contact_fragment;
    }

    private void setListeners(View view) {
        // Set up list Row click listener
        listViewTravels.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                travel=(Travel)((ListView) parent).getAdapter().getItem(position);
                PositionclickLst= FinishedTravels.indexOf(travel);

                final AlertDialog.Builder itemDialog = new AlertDialog.Builder(getActivity());
                itemDialog.setTitle("Travel details");
                itemDialog.setMessage("Name: "+travel.getCustomerName()+"\nPhone: "+travel.getCustomerPhoneNumber()+"\nStart travel: "
                +travel.getStartLocation()+"\nEnd travel: "+travel.getEndLocation());
                itemDialog.setIcon(com.chagay.gettexid.R.drawable.ic_taxi);
                itemDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                itemDialog.show();
            }
        });

        listViewTravels.setTextFilterEnabled(true);
        filterByDestination.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                finishedTravelsAdapter.resetData();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                finishedTravelsAdapter.getFilter().filter(s.toString());
                finishedTravelsAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                finishedTravelsAdapter.notifyDataSetChanged();


            }
        });

    }


        private void findViews(View view) {
            filterByDestination = (EditText) view.findViewById(R.id.filterDestination);
        }
}
