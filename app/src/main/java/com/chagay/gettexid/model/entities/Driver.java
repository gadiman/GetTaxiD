package com.chagay.gettexid.model.entities;

public class Driver {

    //---------------------------------- Fields ------------------------------------------//

    String driverFirstName;
    String driverLastName;
    String driverPhoneNumber;
    String driverID;
    String driverEmailAddress;
    String creditCard;

    //---------------------------------- Constructors ----------------------------------//

    public Driver(String driverFirstName, String driverLastName, String driverPhoneNumber, String driverID, String driverEmailAddress, String creditCard) {
        this.driverFirstName = driverFirstName;
        this.driverLastName = driverLastName;
        this.driverPhoneNumber = driverPhoneNumber;
        this.driverID = driverID;
        this.driverEmailAddress = driverEmailAddress;
        this.creditCard = creditCard;
    }

    public Driver(){}

    //------------------------------- Getters and Setters ------------------------------//

    public String getDriverFirstName() {
        return driverFirstName;
    }

    public void setDriverFirstName(String driverFirstName) {
        this.driverFirstName = driverFirstName;
    }

    public String getDriverLastName() {
        return driverLastName;
    }

    public void setDriverLastName(String driverLastName) {
        this.driverLastName = driverLastName;
    }

    public String getDriverPhoneNumber() {
        return driverPhoneNumber;
    }

    public void setDriverPhoneNumber(String driverPhoneNumber) {
        this.driverPhoneNumber = driverPhoneNumber;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public String getDriverEmailAddress() {
        return driverEmailAddress;
    }

    public void setDriverEmailAddress(String driverEmailAddress) {
        this.driverEmailAddress = driverEmailAddress;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

}
