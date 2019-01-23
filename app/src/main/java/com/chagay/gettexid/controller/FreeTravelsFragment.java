package com.chagay.gettexid.controller;



import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.chagay.gettexid.R;
import android.app.Fragment;

import com.chagay.gettexid.model.backend.DB_Manager;
import com.chagay.gettexid.model.backend.FactoryMethod;
import com.chagay.gettexid.model.datasource.FireBase_DBDriver;
import com.chagay.gettexid.model.entities.Travel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.support.constraint.Constraints.TAG;

public  class FreeTravelsFragment extends Fragment {

    ListView listViewTravels;
    View contact_fragment;
    List<Travel> Travels;
    TravelsListViewAdapter travelsListViewAdapter;
    static boolean flag = false;
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;
    DB_Manager db_manager = FactoryMethod.getManager() ;
    TextView custumerName;
    TextView InitialAddress;
    TextView Destination;
    EditText filterByDestination;
    Button callButton;
    Button startDriveButton;
    Button smsButton;
    Button mailButton;
    Button finishButton;
    Button addContantButton;
    int PositionclickLst;
    Travel travel;
    String travlTakedID = "";
    boolean contentP = false;
    boolean isConnectionCreate = false;
    boolean isTravelTaked = false;


    @RequiresApi(api = Build.VERSION_CODES.M)
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
                        Travels = db_manager.untreatedTravels(travlTakedID);
                        travelsListViewAdapter = new TravelsListViewAdapter(getActivity(), R.layout.list_item, Travels);
                        listViewTravels.setAdapter(travelsListViewAdapter);
                    } else {

                        Travels = db_manager.untreatedTravels(travlTakedID);
                        travelsListViewAdapter.notifyDataSetChanged();
                    }
                }


                @Override
                public void onFailure(List<Travel> obj) {
                   Travels = db_manager.untreatedTravels(travlTakedID);
                    if (travelsListViewAdapter == null)
                        travelsListViewAdapter = new TravelsListViewAdapter(getActivity(), R.layout.list_item, Travels);
                   travelsListViewAdapter.notifyDataSetChanged();
                   listViewTravels.setAdapter(travelsListViewAdapter);
                }
            });
        findViews(contact_fragment);
        setListeners(contact_fragment);
        getPermissionToReadUserContacts();
        listViewTravels.setTextFilterEnabled(true);
        registerForContextMenu(listViewTravels);
        return contact_fragment;
    }

    private void setListeners(View viewA) {
        mailButton.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                if(Travels.size() != 0)
                {
                    isConnectionCreate = true;
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", Travels.get(PositionclickLst).getCustomerEmailAddress(), null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }
            }
        });


        smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Travels.size() != 0) {

                    isConnectionCreate = true;
                    // send sms intent
                    String phoneNumber = Travels.get(PositionclickLst).getCustomerPhoneNumber().toString();
                    Uri uri = Uri.parse("smsto:" + "+972" + phoneNumber);
                    Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                    intent.putExtra("sms_body", "HI I am your driver, I come to pick you up");
                    startActivity(intent);
                }

            }
        });

        // Set up Call click for intent to call
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Travels.size() != 0) {
                    isConnectionCreate = true;
                    Log.d(TAG, " callButton");
                    String phoneNumber = Travels.get(PositionclickLst).getCustomerPhoneNumber().toString();
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "+972" + phoneNumber, null));
                    startActivity(intent);
                }

            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                if(Travels.size() != 0) {

                    if (!isTravelTaked) {
                        Toast.makeText(getActivity(), "Take the travel before", Toast.LENGTH_SHORT).show();
                    } else {
                        isTravelTaked = false;
                        isConnectionCreate = false;
                        travlTakedID = "";

                        new AsyncTask<Void, Void, Boolean>() {

                            @Override
                            protected Boolean doInBackground(Void... voids) {
                                String id = db_manager.updateTravelFinish(Travels.get(PositionclickLst).getId());

                                return id.equals(Travels.get(PositionclickLst).getTravel_status().toString());
                            }

                            protected void onPostExecute(Boolean f) {
                                super.onPostExecute(f);

                                Travels.get(PositionclickLst).setTravel_status(Travel.TRAVEL_STATUS.OCCUPIED);
                                Travels.get(PositionclickLst).setDriverID(db_manager.getCurrentDriver().getDriverID());

                                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
                                Date currentLocalTime = cal.getTime();
                                DateFormat date = new SimpleDateFormat("HH:mm");
                                date.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));
                                String localTime = date.format(currentLocalTime);

                                Travels.get(PositionclickLst).setEndTravelTime(localTime);
                                Travels.get(PositionclickLst).setDateOfTravel(new SimpleDateFormat("dd-mm-yyyy", Locale.getDefault()).format(new Date()));
                                Travels = db_manager.untreatedTravels(travlTakedID);
                                travelsListViewAdapter.notifyDataSetChanged();


                            }

                        }.execute();
                    }
                }


            }
        });


        // change Travel status and Driver id's
        startDriveButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                if (Travels.size() != 0) {


                    String driverID = db_manager.getCurrentDriver().getDriverID();

                    if (!isConnectionCreate) {
                        Toast.makeText(getActivity(), "Contact customer before", Toast.LENGTH_SHORT).show();
                    } else if (db_manager.thereIsPreviousTrip(driverID)) {
                        Toast.makeText(getActivity(), "Finish previous travel before", Toast.LENGTH_SHORT).show();

                    } else {
                        isTravelTaked = true;
                        new AsyncTask<Void, Void, Boolean>() {

                            @Override
                            protected Boolean doInBackground(Void... voids) {
                                String result = db_manager.updateTravelDetails(Travels.get(PositionclickLst).getId(), driverID);
                                return result.equals(driverID);
                            }

                            protected void onPostExecute(Boolean aBoolean) {
                                super.onPostExecute(aBoolean);
                                if (aBoolean) {
                                    Toast.makeText(getActivity(), "Take travel successful", Toast.LENGTH_SHORT).show();
                                    travlTakedID = Travels.get(PositionclickLst).getId();
                                    Travels.get(PositionclickLst).setTravel_status(Travel.TRAVEL_STATUS.OCCUPIED);
                                    Travels.get(PositionclickLst).setDriverID(db_manager.getCurrentDriver().getDriverID());
                                } else
                                    Toast.makeText(getActivity(), "Error please try again", Toast.LENGTH_SHORT).show();

                            }
                        }.execute();

                    }
                }
            }
        });


        // Set up list Row click listener
        listViewTravels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(Travels.size() != 0) {


                    travel = (Travel) ((ListView) parent).getAdapter().getItem(position);

                    PositionclickLst = Travels.indexOf(travel);


                    custumerName = (TextView) viewA.findViewById(R.id.customer_name);
                    custumerName.setText(travel.getCustomerName());
                    InitialAddress = (TextView) viewA.findViewById(R.id.initial_address);
                    InitialAddress.setText(travel.getStartLocation());
                    Destination = (TextView) viewA.findViewById(R.id.destination);
                    Destination.setText(travel.getEndLocation());
                }
            }
        });


        listViewTravels.setTextFilterEnabled(true);
        filterByDestination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
              travelsListViewAdapter.resetData();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                travelsListViewAdapter.getFilter().filter(s.toString());
                travelsListViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        addContantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                // adb.setView(alertDialogView);
                adb.setTitle("Are you sure that you want to add this traveler to your contacts?");
                adb.setMessage("Name: "+Travels.get(PositionclickLst).getCustomerName()+"\nPhone: "+Travels.get(PositionclickLst).getCustomerPhoneNumber());
                adb.setIcon(android.R.mipmap.sym_def_app_icon);
                adb.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onClick(DialogInterface dialog, int which) {

                        addContact(getActivity(),Travels.get(PositionclickLst).getCustomerName(),Travels.get(PositionclickLst).getCustomerPhoneNumber());
                        Toast.makeText(getActivity(),  " added to Contact" , Toast.LENGTH_SHORT).show();

                    }
                });
                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                adb.show();

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private  void addContact (Context ctx , String displayName, String phoneNumber )
    {

        if(contentP) {

            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            int rawContactInsertIndex = ops.size();

            ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

            //Phone Number
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                            rawContactInsertIndex)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, "1").build());

            //Display name/Contact name
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                            rawContactInsertIndex)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, displayName)
                    .build());


// Asking the Contact provider to create a new contact

            // Executing all the insert operations as a single database transaction
            // getContext().getApplicationContext().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
// Asking the Contact provider to create a new contact
            try {
                ContentProviderResult[] res = getActivity().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                Log.d(TAG, "added contact");

            } catch (RemoteException e) {
                Log.d(TAG, "no added contact");

                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (OperationApplicationException e) {

                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else
            getPermissionToReadUserContacts();

    }

    private void findViews(View view) {
        callButton = (Button) view.findViewById(R.id.callButtom);
        startDriveButton = (Button) view.findViewById(R.id.startDriveButton);
        smsButton = (Button) view.findViewById(R.id.smsButton);
        mailButton=(Button) view.findViewById(R.id.mailButton);
        finishButton = (Button) view.findViewById(R.id.finishTravel);
        filterByDestination = (EditText) view.findViewById(R.id.filterDestination);
        addContantButton = (Button) view.findViewById(R.id.AddContent);
    }



    // Called when the user is performing an action which requires the app to read the
    // user's contacts
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionToReadUserContacts() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                // Show our own UI to explain to the user why we need to read the contacts
                // before actually requesting the permission and showing the default UI
            }

            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_PERMISSIONS_REQUEST);
        }
    }

    // Callback with the request from calling requestPermissions(...)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == READ_CONTACTS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Read Contacts permission granted", Toast.LENGTH_SHORT).show();
                contentP =true;
            } else {
                // showRationale = false if user clicks Never Ask Again, otherwise true
                boolean showRationale = shouldShowRequestPermissionRationale(  Manifest.permission.READ_CONTACTS);

                if (showRationale) {
                    // do something here to handle degraded mode
                } else {
                    Toast.makeText(getActivity(), "Read Contacts permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}










