package com.chagay.gettexid.model.backend;

import android.content.ContentValues;

import com.chagay.gettexid.model.entities.Driver;

public class DriverConst {

    //------------------- Const keys for ContentValues -------------------------//
    public static class DriverC {
        public static final String DRIVER_FIRST_NAME = "driverFirstName";
        public static final String DRIVER_LAST_NAME = "driverLastName";
        public static final String DRIVER_PHONE_NUMBER = "driverPhoneNumber";
        public static final String DRIVER_ID = "driverID";
        public static final String DRIVER_EMAIL_ADDRESS = "driverEmailAddress";
        public static final String CREDIT_CARD = "creditCard";
    }
    //------------------------------- Functions -------------------------------//

    //Function that make casting from Driver object to ContentValues object
    public static ContentValues DriverToContentValues(Driver driver) {

        ContentValues contentValues = new ContentValues();

        contentValues.put( DriverConst.DriverC.DRIVER_FIRST_NAME, driver.getDriverFirstName() );
        contentValues.put( DriverConst.DriverC.DRIVER_LAST_NAME, driver.getDriverLastName() );
        contentValues.put( DriverConst.DriverC.DRIVER_PHONE_NUMBER, driver.getDriverPhoneNumber() );
        contentValues.put( DriverConst.DriverC.DRIVER_ID, driver.getDriverID() );
        contentValues.put( DriverConst.DriverC.DRIVER_EMAIL_ADDRESS, driver.getDriverEmailAddress() );
        contentValues.put( DriverConst.DriverC.CREDIT_CARD, String.valueOf( driver.getCreditCard() ) );

        return contentValues;
    }

    //Function that make casting from ContentValue object to Driver object
    public static Driver ContentValuesToDriver(ContentValues contentValues) {
        Driver driver = new Driver();

        driver.setDriverFirstName( contentValues.getAsString( DriverConst.DriverC.DRIVER_FIRST_NAME ) );
        driver.setDriverLastName( contentValues.getAsString( DriverConst.DriverC.DRIVER_LAST_NAME ) );
        driver.setDriverPhoneNumber( contentValues.getAsString( DriverConst.DriverC.DRIVER_PHONE_NUMBER ) );
        driver.setDriverID( contentValues.getAsString( DriverConst.DriverC.DRIVER_ID ) );
        driver.setDriverEmailAddress( contentValues.getAsString( DriverConst.DriverC.DRIVER_EMAIL_ADDRESS ) );
        driver.setCreditCard( contentValues.getAsString( DriverConst.DriverC.CREDIT_CARD ) );
        return driver;
    }
}
