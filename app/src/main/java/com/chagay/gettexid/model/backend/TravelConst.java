package com.chagay.gettexid.model.backend;

import android.content.ContentValues;

import com.chagay.gettexid.model.entities.Travel;


public class TravelConst {

    //------------------- Const keys for ContentValues -------------------------//

    public static class TravelC {
        public static final String TRAVEL_STATUS = "TravelStatus";
        public static final String START_LOCATION = "startLocation";
        public static final String END_LOCATION = "endLocation";
        public static final String START_TIME = "startTravelTime";
        public static final String END_TIME = "endTravelTime";
        public static final String CUSTOMER_NAME = "customerName";
        public static final String CUSTOMER_PHONE = "customerPhoneNumber";
        public static final String CUSTOMER_EMAIL = "customerEmailAddress";
    }

    //------------------------------- Functions -------------------------------//

    //Function that make casting from Travel object to ContentValues object
    public static ContentValues TravelToContentValues(Travel travel) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(TravelC.CUSTOMER_NAME, travel.getCustomerName());
        contentValues.put(TravelC.CUSTOMER_PHONE, travel.getCustomerPhoneNumber());
        contentValues.put(TravelC.START_LOCATION, travel.getStartLocation());
        contentValues.put(TravelC.END_LOCATION, travel.getEndLocation());
        contentValues.put(TravelC.CUSTOMER_EMAIL, travel.getCustomerEmailAddress());
        contentValues.put(TravelC.START_TIME, travel.getStartTravelTime());
        contentValues.put(TravelC.END_TIME, travel.getEndTravelTime());
        contentValues.put(TravelC.TRAVEL_STATUS, String.valueOf(travel.getTravel_status()));

        return contentValues;
    }

    //Function that make casting from ContentValue object to Travel object
    public static Travel ContentValuesToTravel(ContentValues contentValues)
    {
        Travel travel = new Travel();

        travel.setCustomerName(contentValues.getAsString(TravelC.CUSTOMER_NAME));
        travel.setCustomerPhoneNumber(contentValues.getAsString(TravelC.CUSTOMER_PHONE));
        travel.setCustomerEmailAddress(contentValues.getAsString(TravelC.CUSTOMER_EMAIL));
        travel.setStartLocation(contentValues.getAsString(TravelC.START_LOCATION));
        travel.setEndLocation(contentValues.getAsString(TravelC.END_LOCATION));
        travel.setStartTravelTime(contentValues.getAsString(TravelC.START_TIME));
        travel.setEndTravelTime(contentValues.getAsString(TravelC.END_TIME));
        travel.setTravel_status(Enum.valueOf(Travel.TRAVEL_STATUS.class,contentValues.getAsString(TravelC.TRAVEL_STATUS)));

        return travel;
    }



}
